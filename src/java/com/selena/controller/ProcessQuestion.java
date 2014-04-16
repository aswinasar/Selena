/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.selena.controller;

import com.selena.model.Query;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.lexicon.jdbc4sparql.SPARQLStatement;

/**
 *
 * @author 45W1N
 */
public class ProcessQuestion extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static final Logger logger = Logger.getLogger(ProcessQuestion.class.getName());
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String input = request.getParameter("input");
        
        Query userQuery = new Query();
        String subject = userQuery.getSubject();
        String property = userQuery.getProperty();
        StringBuilder sb = new StringBuilder();
        
        try {
            Class.forName("org.lexicon.jdbc4sparql.SPARQLDriver");
            logger.log(Level.INFO,"Driver Successfully Loaded.");
            //System.out.println("Driver Successfully Loaded.");
        } catch (ClassNotFoundException ex) {
            //Logger.getLogger(ProcessQuestion.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("SPARQLDriver not found!");
            logger.log(Level.SEVERE,"SPARQLDriver not found!");
        }
        
        try {
            //Connection conn = DriverManager.getConnection("jdbc:jena:remote:query=http://localhost:3030/ds/query");
            Connection conn = DriverManager.getConnection("jdbc:sparql:http://dbpedia.org/sparql");
            SPARQLStatement stmt = (SPARQLStatement)conn.createStatement();
            //Statement stmt = conn.createStatement();
            String queryString = "PREFIX p: <http://dbpedia.org/property/> " +
            "PREFIX dbpedia: <http://dbpedia.org/resource/> " +
            "PREFIX dbpedia-owl: <http://dbpedia.org/ontology/> " +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
            "select distinct ?result where {" +
            "?resultlink "+property+" "+subject+". "+
                    "?resultlink rdfs:label ?result. "+
            "FILTER (langMatches(lang(?result),\"en\"))} LIMIT 100";    /* if no result, swap order of subject and result */
            
            //String entity = "Aharon_Barak";
            //String queryString ="PREFIX dbres: <http://dbpedia.org/resource/> SELECT * WHERE {dbres:"+ entity+ "<http://www.w3.org/2000/01/rdf-schema#label> ?o FILTER (langMatches(lang(?o),\"en\"))}";
            logger.log(Level.INFO,"Query String: "+queryString);
            ResultSet rset = stmt.executeQuery(queryString);
            String currentResult;
            int currentLength;
            while (rset.next()) {
            // Print out type as a string
                currentResult = rset.getString("result");
                currentLength = currentResult.length();
                if (currentResult.substring(currentLength-3,currentLength).equals("@en")){
                currentResult = currentResult.substring(0, currentResult.length()-3);  // For removing the '@en' language tag from result.
                }
            System.out.println(currentResult+"\n");
            sb.append(currentResult+"\n");
            }
            rset.close();
            stmt.close();
        }
        catch (SQLException e) {
        System.err.println("SQL Error - " + e.getMessage());
        } 
        
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProcessQuestion</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProcessQuestion at " + request.getContextPath() + "</h1>");
            out.println("<p>The user's question is \""+input+"\"</p>");
            out.println("<p>Selena's answer is <pre>"+sb+"</pre></p>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

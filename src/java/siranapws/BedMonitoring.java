/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siranapws;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONValue;

/**
 *
 * @author dody
 */
public class BedMonitoring extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    //Database credential
    private static final String MysqlUser = "yourDatabaseUsername";
    private static final String MysqlPass = "yourDatabasePassword";
    private static final String MysqlURL = "jdbc:mysql://yourIPServer:3306/siranap";
    private static Connection MysqlCon;
    private static Statement MysqlStmt;

    // constructor
    public BedMonitoring() {
        getMysqlConnection();
    }

    private Connection getMysqlConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.print("Connection fail: ");
            System.err.println(e.getException());
        }
        try {
            MysqlCon = DriverManager.getConnection(MysqlURL, MysqlUser, MysqlPass);
            System.out.println("Connected to MySQL Database.");
        } catch (SQLException e) {
            System.err.println("Invalid user and/or password. " + e.getMessage());
        }
        return MysqlCon;
    }

    private String sqlBedMonitoring() {
        return "SELECT\n"
                + "bed_monitoring.kode_ruang,\n"
                + "bed_monitoring.tipe_pasien,\n"
                + "bed_monitoring.total_TT,\n"
                + "bed_monitoring.terpakai_male,\n"
                + "bed_monitoring.terpakai_female,\n"
                + "bed_monitoring.kosong_male,\n"
                + "bed_monitoring.kosong_female,\n"
                + "bed_monitoring.waiting,\n"
                + "bed_monitoring.tgl_update\n"
                + "FROM\n"
                + "bed_monitoring";
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet BedMonitoring</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet BedMonitoring at " + request.getContextPath() + "</h1>");
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
//        processRequest(request, response);
        response.getWriter();
        response.setContentType("application/json;charset=UTF-8");

        try {
            List ll = new LinkedList();

            // Get a Resultset from SIMRS Database
            MysqlStmt = MysqlCon.createStatement();
            ResultSet rs = MysqlStmt.executeQuery(sqlBedMonitoring());

            while (rs.next()) {
                Map map = new LinkedHashMap();
                map.put("kode_ruang", rs.getString(1));
                map.put("tipe_pasien", rs.getString(2));
                map.put("total_tt", rs.getString(3));
                map.put("terpakaiMale", rs.getString(4));
                map.put("terpakaiFemale", rs.getString(5));
                map.put("kosongMale", rs.getString(6));
                map.put("kosongFemale", rs.getString(7));
                map.put("waiting", rs.getString(8));
                map.put("tgl_update", rs.getString(9));
                ll.add(map);
            }
            String jsonStr = JSONValue.toJSONString(ll);
            response.getWriter().println(jsonStr);
        } catch (SQLException ex) {
            Logger.getLogger(BedMonitoring.class.getName()).log(Level.SEVERE, null, ex);
        }
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

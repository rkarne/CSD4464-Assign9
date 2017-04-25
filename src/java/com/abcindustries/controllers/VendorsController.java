/*
 * Copyright 2015 Len Payne <len.payne@lambtoncollege.ca>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.abcindustries.controllers;

import com.abcindustries.entities.Product;
import com.abcindustries.entities.Vendor;
import com.abcindustries.utilities.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 *
 * @author Roja Jayashree Karne
 */
@ApplicationScoped
public class VendorsController {
    // TODO: See ProductsController for a starting point
     List<Vendor> vendorList;

    public VendorsController() {
        retrieveAllvendors();
    }

    private void retrieveAllvendors() {
        // TODO: Make a call that retrieves all the Products from the Database
        
         try {
            Connection conn = DBUtils.getConnection();
            vendorList = new ArrayList<>();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Vendors");
            while (rs.next()) {
                Vendor v = new Vendor();
                v.setVendorId(rs.getInt("VendorId"));
                v.setName(rs.getString("Name"));
                v.setContactName(rs.getString("ContactName"));
                v.setPhoneNumber(rs.getString("PhoneNumber"));
                vendorList.add(v);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VendorsController.class.getName()).log(Level.SEVERE, null, ex);
            vendorList = new ArrayList<>();
        }
        
    }

    public void persistToDb(Vendor v) {
        try {
            String sql = "";
            Connection conn = DBUtils.getConnection();
            sql = "INSERT INTO Vendors (Name, ContactName, PhoneNumber, VendorId) VALUES (?, ?, ? ,?)";
            //sql = "UPDATE Vendors SET Name = ?, ContactName = ? ,PhoneNumber = ? WHERE VendorId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, v.getName());
            pstmt.setString(2, v.getContactName());
            pstmt.setString(3, v.getPhoneNumber());
            pstmt.setInt(4, v.getVendorId());
            pstmt.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
      public void persistEditToDb(Vendor v) {
        try {
            String sql = "";
            Connection conn = DBUtils.getConnection();
            //sql = "INSERT INTO Vendors (Name, ContactName, PhoneNumber, VendorId) VALUES (?, ?, ? ,?)";
            sql = "UPDATE Vendors SET Name = ?, ContactName = ? ,PhoneNumber = ? WHERE VendorId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, v.getName());
            pstmt.setString(2, v.getContactName());
            pstmt.setString(3, v.getPhoneNumber());
            pstmt.setInt(4, v.getVendorId());
            pstmt.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removeFromDb(Vendor v) {
        // TODO: Make a call that deletes a single Product from the database
        try {
            if (v.getVendorId() >= 0) {
                Connection conn = DBUtils.getConnection();
                String sql = "DELETE FROM Vendors WHERE VendorId = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, v.getVendorId());
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        retrieveAllvendors();
    }

    public List<Vendor> getAll() {
        return vendorList;
    }

    public JsonArray getAllJson() {
        // TODO: Build a JsonArray object from the List
         JsonArrayBuilder json = Json.createArrayBuilder();
         for(Vendor vn : vendorList){
             json.add(vn.toJson());
         }  
        return json.build();
       
    }
    
    
    public Vendor getById(int id) {
        Vendor result = null;
        for (Vendor v : vendorList) {
            if (v.getVendorId() == id) {
                result = v;
            }
        }
        return result;
    }

    public JsonObject getByIdJson(int id) {
        return getById(id).toJson();
    }

    public JsonArray getBySearchJson(String query) {
        JsonArrayBuilder json = Json.createArrayBuilder();
        for (Vendor v : vendorList) {
            if (v.getName().contains(query)) {
                json.add(v.toJson());
            }
        }
        return json.build();
    }

    public JsonObject addJson(JsonObject json) {
        Vendor v = new Vendor(json);
       // v.setVendorId(Integer.parseInt(json.getString("vendorId")));
        persistToDb(v);
        vendorList.add(v);
        return v.toJson();
    }

    public JsonObject editJson(int id, JsonObject json) {
        // TODO: Input the JsonObject at the specified id if it already exists
        Vendor v = getById(id);
        v.setName(json.getString("name"));
        v.setContactName(json.getString("contactName"));
        v.setPhoneNumber(json.getString("phoneNumber"));
        persistEditToDb(v);
        return v.toJson();
    }

    public JsonObject delete(int id) {
        // TODO: Remember to delete from both the List and the DB
        Vendor v=getById(id);
        vendorList.remove(v);
        removeFromDb(v);
        return v.toJson();
    }
}

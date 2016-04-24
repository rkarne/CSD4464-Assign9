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

/*=================================================================
 * Warning. This file is fine. You should not need to change this *
 * file at all to complete this project. If you think you do, ask *
 * the instructor about what you're planning to do.               *
 =================================================================*/

$(document).ready(function () {
    var wsUri = "ws://localhost:8080/CPD4414-Final-2016W/socket";
    var ws = new WebSocket(wsUri);
    ws.onMessage = function (evt) {
        if (typeof evt.data !== 'undefined') {
            var json = $.parseJSON(evt.data);
            if (typeof json.vendors !== 'undefined') {
                var data = json.vendors;
                var result = '<tbody>';
                result += "<tr><th>Vendor ID</th><th>Name</th><th>Contact Name</th><th>Phone Number</th></tr>";
                for (var i = 0; i < data.length; i++)
                    result += '<tr><td>' + data[i].vendorId + '</td><td>' +
                            data[i].name + '</td><td>' + data[i].contactName +
                            '</td><td>' + data[i].phoneNumber + '</td></tr>';
                result += '</tbody>';
                $('#vendors').html(result);
            }
            if (typeof json.products !== 'undefined') {
                var data = json.products;
                var result = '<tbody>';
                result += "<tr><th>Product ID</th><th>Name</th><th>Vendor ID</th></tr>";
                for (var i = 0; i < data.length; i++)
                    result += '<tr><td>' + data[i].productId + '</td><td>' + data[i].name + '</td><td>' + data[i].vendorId + '</td></tr>';
                result += '</tbody>';
                $('#products').html(result);
            }
        }
    };
    ws.onOpen = function (evt) {
        ws.send(JSON.stringify({"get": "products"}));
        ws.send(JSON.stringify({"get": "vendors"}));
    }

    $('#findProductById').click(function () {
        ws.send(JSON.stringify({"get": "products", "id": $('#productId').val()}));
    });
    $('#findProductByQuery').click(function () {
        ws.send(JSON.stringify({"get": "products", "search": $('#productQuery').val()}));
    });
    $('#deleteProductById').click(function () {
        ws.send(JSON.stringify({"delete": "products", "id": $('#productId').val()}));
    });
    $('#addProduct').click(function () {
        ws.send(JSON.stringify({post: "products",
            data: {
                name: $('#productName').val(),
                vendorId: parseInt($('#productVendor').val())
            }}));
    });
    $('#setProduct').click(function () {
        ws.send(JSON.stringify({put: "products",
            data: {
                productId: parseInt($('#productId').val()),
                name: $('#productName').val(),
                vendorId: parseInt($('#productVendor').val())
            }}));
    });
    $('#findVendorById').click(function () {
        ws.send(JSON.stringify({"get": "vendors", "id": $('#vendorId').val()}));
    });
    $('#findVendorByQuery').click(function () {
        ws.send(JSON.stringify({"get": "vendors", "search": $('#vendorQuery').val()}));
    });
    $('#deleteVendorById').click(function () {
        ws.send(JSON.stringify({"delete": "vendors", "id": $('#vendorId').val()}));
    });
    $('#addVendor').click(function () {
        ws.send(JSON.stringify({post: "vendors",
            data: {
                name: $('#vendorName').val(),
                contactName: $('#vendorContact').val(),
                phoneNumber: $('#vendorPhone').val()
            }}));
    });
    $('#setProduct').click(function () {
        ws.send(JSON.stringify({put: "vendors",
            data: {
                vendorId: parseInt($('#productVendor').val()),
                name: $('#vendorName').val(),
                contactName: $('#vendorContact').val(),
                phoneNumber: $('#vendorPhone').val()
            }}));
    });
});
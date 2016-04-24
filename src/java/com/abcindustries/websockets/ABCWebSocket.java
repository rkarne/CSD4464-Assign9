/*
 * Copyright 2016 Len Payne <len.payne@lambtoncollege.ca>.
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
package com.abcindustries.websockets;

import com.abcindustries.controllers.ProductsController;
import java.io.StringReader;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Len Payne <len.payne@lambtoncollege.ca>
 */
@ServerEndpoint("/socket")
@ApplicationScoped
public class ABCWebSocket {

    @Inject
    ProductsController products;

    // TODO: Inject the VendorsController as well
    @OnMessage
    public void onMessage(String message, Session session) {
        String output = "";
        JsonObject json = Json.createReader(new StringReader(message)).readObject();
        if (json.containsKey("get")) {
            if (json.getString("get").equals("products")) {
                output = products.getAllJson().toString();
            }
        } // TODO: Capture all the other messages defined on the WebSockets API
        else {
            output = Json.createObjectBuilder()
                    .add("error", "Invalid Request")
                    .add("original", json)
                    .build().toString();
        }

        // TODO: Return the output string to the user that sent the message
    }

}

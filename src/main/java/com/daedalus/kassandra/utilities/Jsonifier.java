package com.daedalus.kassandra.utilities;
/*
    This file is a part of kassandra
    
    kassandra is free software:
    you can redistribute it and/or modify it under the terms of the GNU General
    Public License as published by the Free Software Foundation, either version 3
    of the License, or (at your option) any later version.

    kassandra is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with kassandra.  
    If not, see <https://www.gnu.org/licenses/>.
 */


import com.google.gson.Gson;

import java.util.HashMap;
import java.util.UUID;

public class Jsonifier {
    private final String sessionId;
    private final Gson gson;
    private final String buildId;

    private static String createUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public Jsonifier(String buildId) {
        this.sessionId = createUUID();
        this.gson = new Gson();
        this.buildId = buildId;
    }

    public Jsonifier(String buildId, String sessionId) {
        this.sessionId = sessionId;
        this.buildId = buildId;
        this.gson = new Gson();
    }

    public static String createRandomSessionID() {
        return createUUID();
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public String mapToJsonString(HashMap<String, Object> map) {
        map.put("sessionID", this.sessionId);
        map.put("buildID", this.buildId);
        map.put("pollTime", System.currentTimeMillis());

        return this.gson.toJson(map);
    }
}

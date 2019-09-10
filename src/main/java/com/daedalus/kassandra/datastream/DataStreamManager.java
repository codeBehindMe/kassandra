package com.daedalus.kassandra.datastream;
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

import krpc.client.*;
import krpc.client.services.SpaceCenter.ReferenceFrame;
import krpc.client.services.SpaceCenter.Vessel;

import java.util.HashMap;

/**
 * Manages the data stream.
 */
public class DataStreamManager {

    private HashMap<String, Stream> streams = new HashMap<String, Stream>();
    private final Connection connection;

    public DataStreamManager(Connection c) {
        this.connection = c;
    }

    public void addDataStream(RemoteObject instance, String methodName, Object... args) throws Exception {
        this.streams.put(methodName, this.connection.addStream(instance, methodName, args));
    }

    public HashMap<String, Object> getSnapshot() throws RuntimeException{
        HashMap<String, Object> streamValues = new HashMap<String, Object>();
        this.streams.forEach((methodName, stream) -> {
            try {
                streamValues.put(methodName, stream.get());
            } catch (RPCException | StreamException e) {
                throw new RuntimeException(e);
            }
        });
        return streamValues;
    }
}

package com.daedalus.kassandra;
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


import com.daedalus.kassandra.datastream.DataStreamManager;
import com.daedalus.kassandra.exceptions.UnsupportedGameSceneException;
import com.daedalus.kassandra.utilities.Jsonifier;
import krpc.client.Connection;
import krpc.client.ConnectionException;
import krpc.client.RPCException;
import krpc.client.services.KRPC;
import krpc.client.services.SpaceCenter;
import krpc.client.services.SpaceCenter.ReferenceFrame;
import krpc.client.services.SpaceCenter.Vessel;

import java.io.FileWriter;
import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

public class Kassandra {
    public static void main(String[] args) throws Exception {
        manageConnection();
    }

    private static void manageConnection() throws Exception {
        while (true) {
            Connection c = null;
            try {
                c = Connection.newInstance();

                KRPC krpc = KRPC.newInstance(c);
                manageGameScene(c, krpc);
            } catch (RPCException | ConnectException | RuntimeException e) {
                if (c != null) {
                    c.close();
                }
                System.out.println("Could not connect to server, retrying");
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }

    private static void manageGameScene(Connection c, KRPC krpc) throws Exception {
        while (true) {
            try {
                KRPC.GameScene gc = krpc.getCurrentGameScene();
                if (!gc.toString().equals("FLIGHT")) {
                    throw new UnsupportedGameSceneException();
                }
                startStream(c);
            } catch (UnsupportedGameSceneException e) {
                System.out.println("Not in supported game scene.");
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }

    private static void startStream(Connection c) throws Exception {
        DataStreamManager dsc = new DataStreamManager(c);
        SpaceCenter sc = SpaceCenter.newInstance(c);

        Vessel activeVessel = sc.getActiveVessel();
        ReferenceFrame frame = activeVessel.getOrbit().getBody().getReferenceFrame();
        SpaceCenter.Flight flight = activeVessel.flight(frame);


        dsc.addDataStream(activeVessel, "position", frame);
        dsc.addDataStream(activeVessel, "velocity", frame);
        dsc.addDataStream(activeVessel, "getName");
        dsc.addDataStream(flight, "getMeanAltitude");
        dsc.addDataStream(flight, "getSurfaceAltitude");
        dsc.addDataStream(flight, "getElevation");
        dsc.addDataStream(flight, "getHorizontalSpeed");
        dsc.addDataStream(flight, "getVerticalSpeed");
        dsc.addDataStream(flight, "getPitch");
        dsc.addDataStream(flight, "getHeading");
        dsc.addDataStream(flight, "getRetrograde");
        dsc.addDataStream(flight, "getAtmosphereDensity");
        dsc.addDataStream(flight, "getDynamicPressure");
        dsc.addDataStream(activeVessel, "getSituation");

        Jsonifier j = new Jsonifier("i");

        try (FileWriter fw = new FileWriter(j.getSessionId() + ".data")) {
            while (true) {
                String output = j.mapToJsonString(dsc.getSnapshot());
                System.out.println(output);
                fw.write(output + "\n");
                TimeUnit.MILLISECONDS.sleep(100);
            }
        }
    }

}

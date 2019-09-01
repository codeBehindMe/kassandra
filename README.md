# Kassandra

Kassandra is a flight telemetry reporting system for Kerbals Space Program.
Kassandra was initially incepted in Python and later we this project was 
moved to Java, under the project name Jassandra. 

The Jassandra project saw a lot of compromises with how the flight telemetry
worked with flight test articles. Since Jassandra was developed in-line with
the test article, it had to fit into whatever the final test flight article
was. Whether it was single stage, or multi stage.

Multi stage vehicles had to be very clever about how it handled staging, since
the flight articles signature changed during stage separation. To account
for these limitations, Jassandra employed a robust but very inefficient
system for polling the server. This meant the poll rates had to be somewhat
limited, in the order of 500 ms for it to be consistent. 

```
Jassandra is implementation ready, i.e it can be successfully employed to
gather flight telemetry.
```

Now the flight vehicle has been finalised, and show to be a single stage
system. Now we need to poll the data at much higher rates, in the order of
100 ms.

It was too difficult to simply extend the functionality of Jassandra to be
able to handle a much high poll rate. It requires a significant change in 
the usage of the underlying KRPC library to do enable such rates.

In this project, we re-birth Kassandra, as a purpose built high poll rate
telemetry system for single stage rockets.

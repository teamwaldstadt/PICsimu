# PICsimu

PICsimu ist ein in Java entwickelter Simulator für den [PIC16F84 Mikroprozessor](https://www.microchip.com/wwwproducts/en/PIC16F84) und entstand im Rahmen der Vorlesung "Systemnahe Programmierung 2" an der [DHBW Karlsruhe](https://www.karlsruhe.dhbw.de/startseite.html).

## Compiling / Building

Im Verzeichnis des Projekts den Terminal-Befehl `ant build` ausführen. Die ausführbare JAR wird im Verzeichnis `jar/` unter dem Namensformat `PICsimu-[JAHR][MONAT][TAG]-[STUNDEN][MINUTEN][SEKUNDEN].jar` erstellt, wobei `[JAHR]`, `[MONAT]`, `[TAG]`, `[STUNDEN]`, `[MINUTEN]` und `[SEKUNDEN]` den Zeitpunkt des Builds festlegen.

Zum eigenhändigen Compilen bzw. Builden muss eine aktuelle Version von [Apache Ant](https://ant.apache.org/) auf dem System installiert sein. Zusätzlich wird ein installierter Java-Compiler (beipielsweise [Java SE Development Kit](https://www.oracle.com/technetwork/java/javaee/downloads/jdk8-downloads-2133151.html) Version 8+ oder [OpenJDK](https://openjdk.java.net/) Version 8+) vorausgesetzt.

## Download

Ausführbare JARs können [HIER](https://picsimu.teamwaldstadt.de/jar/) heruntergeladen werden. Neueste Version siehe Zeitstempel des jeweiligen Builds. Es wird eine [Java Runtime Environment](https://java.com/de/download/) Version 8+ zum Ausführen der JARs benötigt.

## TODO-Liste

* Interrupts
* TMR0, Watchdog inkl. Vorteiler **[IN PROGRESS]**
* Command SLEEP
* Stack visualisieren
* Quellcode kommentieren
* ~~Apache Ant integrieren~~ **[DONE]**
* Hardwareansteuerung via RS232
* License-Button
* Dokumentation / Hilfe-Button

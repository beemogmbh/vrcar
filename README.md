# V**rC**ar
### Masterprojekt im Sommersemester 2016

## Allgemeine Beschreibung

## Umsetzung
Wir haben für unseren Prototyp ein [Fahrzeug der Firma Carrera](https://shop.carrera-rc.com/ferngesteuerte-Autos/Green-Splash-oxid.html?shp=6) gewählt. Dieses ist sehr robust und hat durch die Möglichkeit Wasser zu spritzen sehr viel Platz unter der Karosserie den wir für den Pi und separaten Akku verwenden können.

Nach Ausbau der Pumpe und des Wassertanks ist genug Platz vorhanden um einen Raspberry Pi Model B und eine handelsübliche Powerbank mit 10.000mAh unterzubringen.

### Raspberry Pi - Serverseite
Zuerst muss die Kamera des Raspberry eingerichtet werden. Dazu wurde der offiziellen
 [Anleitung](https://www.raspberrypi.org/documentation/usage/camera/README.md) gefolgt.

Um mit jedem Android-Device eine Verbindung herstellen zu können, muss der Pi einen Access-Point erstellen und kann nicht einfach auf den Ad-hoc Modus zurückgreifen.
Ein Access-Point kann mit dem Linux-Service "hostapd" erstellt werden.

Der von uns verwendete Edimax Wifi-Dongle wird allerdings nicht out-of-the-box unterstützt.
In der Anleitung von [ITWelt](https://itwelt.org/anleitungen-howto/raspberry-pi/590-raspberry-pi-und-edimax-ew-7811un-access-point-einrichten)
gibt es allerdings eine angepasste Version die funktioniert. Bei der weiteren Einrichtung haben wir uns auch an dieser Anleitung orientiert.

**DHCP:**
Die Verbindung der Clients soll möglichst einfach funktionieren. Daher verteilen wir auf dem neuen Access-Point automatisch Ip-Adressen per DHCP. Dadurch können wir ausserdem sicher stellen, dass der Pi selbst immer die selbe Adresse besitzt und kein Client diese doppelt nutzt.

Zum einfachen Aufsetzen eines DHCP-Service nutzen wir den Linux-Service [dnsmasq](https://wiki.archlinux.de/title/Dnsmasq) um das Netz "192.168.2.100 - 192.168.2.150" an Clients zu verteilen.

**GStreamer:**
Für unser Projekt ist ein möglichst latenzfreier Videostream eine Grundvoraussetzung.
Für einen Benutzer ist es unmöglich "nach Videobild" zu fahren wenn er nur ein verzögertes Bild der Vergangenheit sieht.
Wir haben hier verschiedene Lösungen wie VLC, UV4L, FFMPEG und GStreamer ausprobiert.
Die geringste Latenz konnten wir dabei mit GStreamer erzielen.

Nach ersten Tests konnte damit, ohne weitere Optimierungen, ein Stream mit etwa 150-200ms Latenz erzeugt werden.

Die Umsetzung ist dabei denkbar einfach.
Über das offizielle Tool der Pi-Cam wird der Stream über eine Pipe des OS direkt an GStreamer übergeben.

Um den Stream direkt beim starten des Pi zur Verfügung zu stellen wird unser Startstream in der "/etc/rc.local" eingetragen.

### Android - Clientseite
Die Android-App für den Prototyp soll erstmal nur das Kamerabild in der VR-Brille anzeigen.

Zur Anzeige des Steams vom Pi wird hier auch [GStreamer](http://gstreamer.com/) genutzt.
Für diesen Prototyp teilen wir das Display einfach in zwei Hälften und zeigen in jeder das selbe Kamerabild.

Zur Nutzung des GStreamer SDK und für die Anzeige des Streams wurden die Tutorials auf der [GStreamer Seite](http://docs.gstreamer.com/display/GstSDK/Android+tutorials) genutzt.
Hier ist besonders Tutorial3 wichtig.
Um zwei Surfaces mit Videodaten gleichzeitig betreiben zu können haben wir uns am [Beispiel](https://github.com/crs4/most-streaming/tree/master/examples/android/DualStreamingExample) von [Most Streaming](https://github.com/crs4/most-streaming) orientiert die genau dieses Problem schon gelöst haben.

Um den Prototypquellcode in diesem Repository compilieren zu können muss  von [der Entwicklerseite](https://gstreamer.freedesktop.org/data/pkg/android/1.8.1/) das entsprechende GStreamer SDK für die Zielplattform heruntergeladen werden.
Anschließend muss in der Datei "app/src/main/jni/Android.mk" der Pfad zum SDK angepasst werden.

## Zukunfsperspektive ##
Hier werden weitere Punkte beschrieben die zeitlich nicht mehr umgesetzt werden konnten.

- **Evaluation der 3D-Modi:** Eine Evaluation müsste durchgeführt werden um zu testen ob 3D mit einer einzelnen Kamera verbessert werden kann oder zwingend 2 Kameras genutzt werden müssen. Kann mit einem Kamerabild ein Augenabstand durch verschieben simuliert werden oder müssen andere Algorithmen wie [2D-to-3D](http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.417.1456&rep=rep1&type=pdf) genutzt werden.
- **Herunterfahren des PI:** Bisher trennen wir den Pi einfach händisch vom Strom. Wünschenswert wäre eine Lösung den Pi über die App herunterfahren zu können.
- **Synchronisation der Views/CPU Leistung:** Die beiden Views laufen zur Zeit je nach akuter Latenz immer weiter auseinander.
- **verschiedene Optiken für Pi-Cam:** Zur Zeit benutzt die Pi-Cam ein Fisheye-Objektiv. Evaluieren wie andere Objektive wirken.

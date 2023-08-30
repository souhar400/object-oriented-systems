![enter image description here](https://upload.wikimedia.org/wikipedia/commons/thumb/1/1f/Logo_FH_Muenster_cmyk.svg/1024px-Logo_FH_Muenster_cmyk.svg.png)
# Objektorientierte Systeme SoSe2022
Das Projekt findet im Rahmen des Moduls **Objektorientierte Systeme** im 4. Semester statt.


## Ziele des Praktikums 
- Beherrschen von Softwaremodellierung und Desing mit der UML 
- Verschiedene Diagrammtypen der UML und deren Einsatzmöglichkeiten kennen in den unterschiedlichen Projektphasen kennen. 
- Tiefes Verstehen und professionnelles Eintsetzen von verschiedenen Design- und Architketurmustern. 
- Umsetzen von verschiedenen Design-Pattern in Java, sowie deren Vor- und Nachteile verstehen. 
- Schärfen des Problemlösungkompetenz durch Diskussion von Pro- und Contra der gewählten Lösungsstrategien. 


## Inhalte
• Die wichtigsten Entwurfsmuster der "Gang of Four"
• Unterschiedliche Modelle der UML 
• Use Case Analyse 
• SWT Komponente
• Eclipse Plug-in unter Verwendung der Rich-Client-Platfrom (RCP) API 
• Erweitertes Functionsplotter als Plugin von Eclipse 


## Beschreibung des Praktikums 
- Entwicklung eine graphische SWT Anwendung mit einem Hauptfenster und einer Eventloop. 
- Entwicklung von einem SWTCanvasPLotter das beliebige Funktionen als Graph auf einem beliebigen Intervall graphische darstellen kann. 
- Geeignete JUnit Tests, um die Transformation von Welt- auf Bildschirmkoordinaten auch außerhalb der graphischen Komponente automatisiert testen zu können, werden geschrieben. 
- Einbinden von Model-View-Controller Architketur.
- Entwicklung einer ScriptEngine, die das Plotten aller möglichen Funktionen ermöglicht, die vom User eingegeben werden. 
- Das Plotten verhält sich richtig an den Stellen , in denen die Funktion nicht definiert ist. 
- Die Funktionen werden als Lambda-Ausdruck geschrieben und werden als @FunctionalInterface util.function.Function verwendet. 
- Der SWTCanvasPlotter und das PlotterModel werden gene die Abstraktionen aus dem javax.script implementiert. (Nashorn wurde hier eingestzt) 
- Den Plotter um einige nützliche Fähigkeiten erweitern: 
    - In der Statusleiste werden die (x,y) Werte der Stelle, in der sich die Maus befindet, angezeigt. 
    - Der User soll in der Lage sein, beliebe Funktionen zu plotten. Das Intervall, die Farbe und weitere Eigenschaften sollen änderbar sein
    - Die Funktionen lassen sich per add und remove und clear effizient verwalten
    - Die Achsen sind mit Skalierungsmarkierungen versehen, mit Gitterlienen. Farbe und Stil dieser Linien sind ebenfalls einstellbar.      
- Der Functionplotter wird als Eclipse Plug-In unter Verwendung der Rich-Client-Platform (RCP) API in Eclipse integriert. 
- Zum Glatten Zeichnen von Kurven werden verschiedene Algorithmen zur adaptiven Schrittweitenberechnung eingesetzt. 
    - Prunning
    - Divide and Conquer
    - Veränderungsrate und Krümmung
    - Fehlerrate des Polygonzugs 
 
 Diese werden gekapselt und durch per geeignete Designpattern angebunden. Passende JUnit Tests werden auch geschrieben. 
- IContentProvider und ILabelProvider als zentrale Elemnte, um Objekte in der Eclipse darzustellen und/oder zu manipulieren werden verwendet
- Die geplotteten Funktionen werden auf einem Baum aufgelistet. 
- Aussagekräftige UML Modell(e) entwickeln


## Nutzungsrechte 
Dieses Produkt unterliegt den Lizenzbestimmungen der FH Münster.

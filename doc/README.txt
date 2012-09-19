This is a highly simplified version of the MFW originally (and probably still) hosted in the noen.sf.net project 
under the p2p directory.
This project aims to simplify based on the lessons learned.. 

Some points simplified:
-OSGI removed. Added crazy implementation complexity with zero benefit. Over-abstraction and over-engineering. Made deployment too difficult.
-XMLRPC changed to RabbitMQ. Less limited in messages, more options for clustering etc. in the middleware.
-SOAP removed and only REST interface remains. SOAP is overly complex and W3C webservices are just nasty stuff.
-REST interface changed from XML to JSON. Much simpler and better maps to data types used.
-MeasureURI changed to completely user configurable string. Allows use of any measure URI type without any real loss.

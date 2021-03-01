# CFT_Demo
Demo application for CFT - merge sort

To try the application, you can run the ready-made executable file (make sure that JRE 1.8 is installed on your computer). 
Instructions:
1. Create multiple files with input values (values must be sorted). 
2. For ascending sort specify the key -a, for descending sort -b. This parametr is not required. Default direction - ascending.
3. For sort values as string specify the key -s, as integer -i. This parametr is required. 
4. Specify file for output
5. Specify input files

For example:

 java -jar demoCFT.jar -a -i out.txt in1.txt in2.txt
_____

You can also specify the full path to the file.
For example(linux):

java -jar demoCFT.jar -d -i /home/user/out.txt /home/user/in1.txt /home/user/in2.txt 

_____

You can also compile the code yourself and run application using your ide. 

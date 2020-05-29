package App;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LZSS {

	int lowerLimitAgain = 3;
	int forwardBufferRange = 258;
	int searchBufferRange = 32768;
	
	int[] theNumberOfRepetitions(String searchBuffer,String forwardBuffer){
	    int index= 0,againCount=1;
	    int again[] = new int[]{1,1};//again[0] index ,again[1] again count
	    for(int i = 0; i< searchBuffer.length()-1;i++){
	        if((int)forwardBuffer.charAt(0) == (int)searchBuffer.charAt(i)){
	            index=i;
	            for(int j = 1;j<forwardBuffer.length();j++){
	                if((int)forwardBuffer.charAt(againCount) == (int)searchBuffer.charAt(j+i) && (j+i)< searchBuffer.length()-1 && againCount < forwardBuffer.length()-1){
	                    againCount++;
	                }else{
	                    break;
	                }
	            }
	            if(againCount >= again[1]){
	                again[1] = againCount;
	                again[0] = index+1;
	                againCount = 1;
	            }
	        }
	    }
	    return again;
	}
	
	String clearMyOver(String searchBuffer){
	    for(int i = 0;i<searchBufferRange-searchBuffer.length();i++)
	    	searchBuffer = searchBuffer.substring(1, searchBuffer.length());
	    return searchBuffer;
	}
	
	void compression() throws IOException{
	    int character;
	    File myObj = new File("text.txt");
	    FileReader readFile = new FileReader(myObj);
	    String forwardBuffer = "",searchBuffer= "",compressedText="",uncompressedText="";
	    Boolean close = true,definitiveTransition = false;
	    if(readFile.ready()){
	        while(((character = readFile.read()) != -1) || forwardBuffer.length()> lowerLimitAgain){
	            if(character == -1){
	                close = false;
	                definitiveTransition = true;
	            }
	            if(close){
	            	forwardBuffer = forwardBuffer + (char)character;
	                uncompressedText+=(char)character;
	            }
	            if(forwardBuffer.length() == forwardBufferRange || definitiveTransition){
	                if(searchBuffer.length() < (lowerLimitAgain+1)){
	                    for(int i = 0;i<lowerLimitAgain;i++){
	                    	compressedText=compressedText+forwardBuffer.charAt(0);
	                        searchBuffer = searchBuffer+forwardBuffer.charAt(0);
	                        forwardBuffer = forwardBuffer.substring(1, forwardBuffer.length());
	                    }
	                }else if(searchBuffer.length() <= searchBufferRange && searchBuffer.length() > 3){

	                    int again[] = new int[2];
	                    again = theNumberOfRepetitions(searchBuffer,forwardBuffer);

	                    if(again[1] < lowerLimitAgain){
	                        for(int j = 0; j< again[1];j++){
	                        	compressedText=compressedText+forwardBuffer.charAt(0);
	                            searchBuffer = searchBuffer+forwardBuffer.charAt(0);
	                            forwardBuffer = forwardBuffer.substring(1, forwardBuffer.length());
	                        }
	                    }else{
	                    	StringBuilder craeteText = new StringBuilder("("+(--again[0])+","+again[1]+")");
	                        compressedText=compressedText+craeteText.toString();
	                        for(int j = 0; j< again[1];j++){
	                        	searchBuffer = searchBuffer+forwardBuffer.charAt(0);
	                            forwardBuffer = forwardBuffer.substring(1, forwardBuffer.length());
	                        }
	                    }
	                    if(searchBuffer.length() > searchBufferRange)
	                        searchBuffer = clearMyOver(searchBuffer);

	                }
	            }
	        }
	        if(forwardBuffer.length() != 0){
	            int sz = forwardBuffer.length();
	            for(int i=0;i<sz;i++){
	                compressedText=compressedText+forwardBuffer.charAt(0);
	                forwardBuffer = forwardBuffer.substring(1, forwardBuffer.length());
	            }
	        }
	    }
	    readFile.close();
	    System.out.println("Uncompressed Data \n------------------\n"+uncompressedText+"\n");
	    System.out.println("Compressed Data \n-----------------\n"+compressedText);
	}
	
	public static void main(String[] args) throws IOException {
		
		Program program = new Program();
		program.compression();

	}

}

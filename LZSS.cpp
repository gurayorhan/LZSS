#include <iostream>
#include <string>
#include <fstream>
#include <sstream>
using namespace std;

unsigned short lowerLimitAgain = 3;
unsigned short forwardBufferRange = 258;
unsigned short searchBufferRange = 32768;

//the position and number of repeated words are calculated
int * theNumberOfRepetitions(string searchBuffer,string forwardBuffer){
    int index= 0,againCount=1;
    int* again = new int[2]{1,1};//again[0] index ,again[1] again count
    for(int i = 0; i< searchBuffer.size()-1;i++){
        if((int)forwardBuffer.at(0) == (int)searchBuffer.at(i)){
            index=i;
            for(int j = 1;j<forwardBuffer.size();j++){
                if((int)forwardBuffer.at(againCount) == (int)searchBuffer.at(j+i) && (j+i)< searchBuffer.size()-1 && againCount < forwardBuffer.size()-1){
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

//excess data in search buffer is deleted
string clearMyOver(string searchBuffer){
    for(int i = 0;i<searchBufferRange-searchBuffer.size();i++)
            searchBuffer.erase(searchBuffer.begin());
    return searchBuffer;
}

void LZSS(){
    char character;
    ifstream readFile("text.txt");
    string forwardBuffer,searchBuffer,compressedText,uncompressedText;
    bool close = true,definitiveTransition = false;
    if(readFile.is_open()){
        while(readFile.good() || forwardBuffer.size()> lowerLimitAgain){
            if(close){
                readFile.get(character);
                forwardBuffer.append(1,character);
                uncompressedText+=character;
            }
            if(!readFile.good()){
                close = false;
                definitiveTransition = true;
            }
            if(forwardBuffer.size() == forwardBufferRange || definitiveTransition){
                if(searchBuffer.size() < (lowerLimitAgain+1)){
                    for(int i = 0;i<lowerLimitAgain;i++){
                        compressedText+=forwardBuffer[0];
                        searchBuffer.append(1,forwardBuffer[0]);
                        forwardBuffer.erase(forwardBuffer.begin());
                    }
                }else if(searchBuffer.size() <= searchBufferRange && searchBuffer.size() > 3){

                    int *again = theNumberOfRepetitions(searchBuffer,forwardBuffer);

                    if(again[1] < lowerLimitAgain){
                        for(int j = 0; j< again[1];j++){
                            compressedText+=forwardBuffer[0];
                            searchBuffer.append(1,forwardBuffer[0]);
                            forwardBuffer.erase(forwardBuffer.begin());
                        }
                    }else{
                        ostringstream craeteText;
                        craeteText<<"("<<--again[0]<<","<<again[1]<<")";
                        compressedText+=craeteText.str();
                        for(int j = 0; j< again[1];j++){
                            searchBuffer.append(1,forwardBuffer[0]);
                            forwardBuffer.erase(forwardBuffer.begin());
                        }
                    }
                    if(searchBuffer.size() > searchBufferRange)
                        searchBuffer = clearMyOver(searchBuffer);

                }
            }
        }
        if(forwardBuffer.size() != 0){
            int sz = forwardBuffer.size();
            for(int i=0;i<sz;i++){
                compressedText+=forwardBuffer[0];
                forwardBuffer.erase(forwardBuffer.begin());
            }
        }
    }
    readFile.close();
    cout<<uncompressedText<<endl<<endl;
    cout<<compressedText<<endl;
}

int main()
{
    LZSS();
    return 0;
}

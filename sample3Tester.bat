@ECHO OFF
    cd C:\Users\William\Documents\GitHub\CS367_Program5\bin
    
    :PROMPT
        SET /P verbose=Verbose (Y/[N])?
        IF /I "%verbose%" NEQ "Y" GOTO NOTVERBOSE
    
    :VERBOSE
        java LoadBalancerMain 10 10000 C:\Users\William\Documents\GitHub\CS367_Program5\sampleFile3.txt -v > C:\Users\William\Desktop\output.txt
        
        FC C:\Users\William\Desktop\output.txt C:\Users\William\Documents\GitHub\CS367_Program5\sampleOutput3Verbose.txt > C:\Users\William\Desktop\diff.txt
        
        GOTO END
        
    :NOTVERBOSE
        java LoadBalancerMain 10 10000 C:\Users\William\Documents\GitHub\CS367_Program5\sampleFile3.txt > C:\Users\William\Desktop\output.txt
        
        FC C:\Users\William\Desktop\output.txt C:\Users\William\Documents\GitHub\CS367_Program5\sampleOutput3.txt > C:\Users\William\Desktop\diff.txt
:END
ECHO Test complete.
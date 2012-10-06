@echo off

set JARS=

for %%j in (.\lib\*.jar) do call :add_jar %%j
  
start javaw -Xmx256m -cp %CLASSPATH%;%JARS% org.yuzifeng.mystock.gui.MainFrame
 
exit /b   
  
:add_jar   
set JARS=%JARS%;%1
exit /b
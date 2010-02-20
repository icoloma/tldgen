@echo off

setlocal EnableDelayedExpansion
set DIRNAME=%~dp0%

set TLDGEN_HOME=%DIRNAME%..

for %%a in (%TLDGEN_HOME%\tldgen-tools-*.jar) do (
	set CP=%%a
)
for %%a in (%TLDGEN_HOME%\lib\*.jar) do (
	set CP=!CP!;%%a
)

set LAUNCH=javadoc -docletpath !CP! -private -doclet org.loom.tldgen.TldDoclet

if "%1" == "-help" goto help
if "%1" == "--help" goto help
if "%1" == "/?" goto help
if "%1" == "" goto help

:loop
	if "%~1" == "" goto launch
	set LAUNCH=%LAUNCH% %1
	shift
	goto loop

:help
	set LAUNCH=!LAUNCH! -sourcepath . -subpackages org
	goto launch
	
:launch
	echo %LAUNCH%
	%LAUNCH%
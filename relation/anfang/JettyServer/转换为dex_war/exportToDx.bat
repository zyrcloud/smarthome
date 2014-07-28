for %%i in ( *.war) do (
	jar -xvf %%i
        dx --dex --output=%cd%/WEB-INF/lib/classes.zip %cd%/WEB-INF/classes
        rmdir /Q /S %cd%\WEB-INF\classes
        move %%i %%i_src.war
	jar -cvf %%i WEB-INF META-INF
        rmdir /Q /S WEB-INF META-INF
)
pause
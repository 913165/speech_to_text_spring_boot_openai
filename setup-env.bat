@echo off
echo Setting up environment variables for Speech-to-Text Application
echo.

echo Please enter your OpenAI API key:
set /p OPENAI_API_KEY="API Key: "

echo.
echo Setting environment variable...
setx OPENAI_API_KEY "%OPENAI_API_KEY%"

echo.
echo Environment variable set successfully!
echo You can now run: mvn spring-boot:run
echo.
pause 
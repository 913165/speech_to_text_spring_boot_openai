# Environment Variables Setup

This document explains how to set up environment variables for the Speech-to-Text Application.

## Required Environment Variables

### OPENAI_API_KEY

This is the only required environment variable for the application to function properly.

**Description**: Your OpenAI API key for accessing the Whisper API for speech-to-text functionality.

**How to get it**:
1. Go to [OpenAI Platform](https://platform.openai.com/api-keys)
2. Sign in or create an account
3. Click "Create new secret key"
4. Copy the generated key

## Setup Methods

### Method 1: Setup Scripts (Recommended)

#### Windows
```cmd
setup-env.bat
```
This script will prompt you for your API key and set it as a system environment variable.

#### Linux/macOS
```bash
./setup-env.sh
```
This script will add the environment variable to your shell configuration file.

### Method 2: Manual Environment Variable

#### Windows PowerShell
```powershell
$env:OPENAI_API_KEY="your-api-key-here"
```

#### Windows Command Prompt
```cmd
set OPENAI_API_KEY=your-api-key-here
```

#### Linux/macOS
```bash
export OPENAI_API_KEY="your-api-key-here"
```

### Method 3: .env File

Create a `.env` file in the project root:
```
OPENAI_API_KEY=your-api-key-here
```

**Note**: The `.env` file is already included in `.gitignore` to prevent accidentally committing your API key.

## Verification

To verify your environment variable is set correctly:

### Windows PowerShell
```powershell
echo $env:OPENAI_API_KEY
```

### Windows Command Prompt
```cmd
echo %OPENAI_API_KEY%
```

### Linux/macOS
```bash
echo $OPENAI_API_KEY
```

## Security Notes

- Never commit your API key to version control
- The `.env` file is automatically ignored by Git
- Use environment variables in production environments
- Rotate your API keys regularly
- Monitor your OpenAI usage to avoid unexpected charges

## Troubleshooting

### "Could not resolve placeholder 'OPENAI_API_KEY'"

This error means the environment variable is not set. Try:
1. Restart your terminal/command prompt
2. Verify the environment variable is set using the verification commands above
3. Make sure you're running the application from the same terminal where you set the variable

### "Invalid API key" or "Authentication failed"

This means your API key is invalid. Check:
1. The API key is copied correctly (no extra spaces)
2. The API key is active in your OpenAI account
3. You have sufficient credits in your OpenAI account 
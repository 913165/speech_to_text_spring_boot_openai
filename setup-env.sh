#!/bin/bash

echo "Setting up environment variables for Speech-to-Text Application"
echo

echo "Please enter your OpenAI API key:"
read -s OPENAI_API_KEY

echo
echo "Setting environment variable..."

# Add to .bashrc for Linux
if [[ "$OSTYPE" == "linux-gnu"* ]]; then
    echo "export OPENAI_API_KEY=\"$OPENAI_API_KEY\"" >> ~/.bashrc
    echo "Environment variable added to ~/.bashrc"
    echo "Please run: source ~/.bashrc"
fi

# Add to .zshrc for macOS
if [[ "$OSTYPE" == "darwin"* ]]; then
    echo "export OPENAI_API_KEY=\"$OPENAI_API_KEY\"" >> ~/.zshrc
    echo "Environment variable added to ~/.zshrc"
    echo "Please run: source ~/.zshrc"
fi

echo
echo "Environment variable set successfully!"
echo "You can now run: mvn spring-boot:run"
echo 
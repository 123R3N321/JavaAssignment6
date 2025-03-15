import re
import sys
import os
# Define the regex patterns
name_pattern = re.compile(r'\b[A-Z][a-z]+ [A-Z][a-z]+\b')  # [redacted] Lastname with only letters
digits_pattern = re.compile(r'[A-Za-z]+\d{4}\b')  # Matches a word that ends with exactly 4 digits

def check_commit_changes():
    # Get list of files that are staged for commit
    files_to_check = sys.argv[1:]

    # Loop through files and check for the patterns
    for file in files_to_check:
        # Only process text files (skip binary files)
        if os.path.splitext(file)[1] in ['.java', '.md', '.txt', '.py']:  # Add more file extensions as needed
            try:
                with open(file, 'r', encoding='utf-8') as f:
                    content = f.read()

                modified_content = content
                lines = content.splitlines()

                # Check each line for patterns and substitute with [redacted]
                for i, line in enumerate(lines):
                    if name_pattern.search(line):
                        print(f"Redacted name found in {file}: {line}")
                        lines[i] = re.sub(name_pattern, '[redacted]', line)

                    if digits_pattern.search(line):
                        print(f"Redacted pattern found in {file}: {line}")
                        lines[i] = re.sub(digits_pattern, '[redacted]', line)

                # If any changes were made, rewrite the file with the modified content
                if modified_content != '\n'.join(lines):
                    with open(file, 'w', encoding='utf-8') as f:
                        f.write('\n'.join(lines))
                print(f"scanned file: {file}")
            except UnicodeDecodeError:
                # Handle case where file is binary or cannot be decoded as UTF-8
                print(f"Warning: Skipping non-text file {file} (cannot read as UTF-8)")
                continue

    return 0  # Allow commit, as we only modified the content

if __name__ == "__main__":
    sys.exit(check_commit_changes())
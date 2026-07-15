import re

with open('src/main/java/pe/edu/utp/proyecto/controller/ApuestaController.java', 'r', encoding='utf-8') as f:
    content = f.read()

methods = {}
new_content = ''
for part in re.split(r'(?=@(?:GetMapping|PostMapping)\()', content):
    method_name_match = re.search(r'@(?:GetMapping|PostMapping)\("[^"]*"\)\s*(?:public|private)\s+String\s+([a-zA-Z0-9_]+)', part)
    if method_name_match:
        m_name = method_name_match.group(1)
        if m_name not in methods:
            methods[m_name] = True
            new_content += part
    else:
        new_content += part

if not new_content.strip().endswith('}'):
    new_content += '\n}\n'
# Ensure closing brace logic is completely sound:
# Instead, strip any trailing braces and add exactly one.
new_content = new_content.rstrip()
while new_content.endswith('}'):
    new_content = new_content[:-1].rstrip()
new_content += '\n}\n'

with open('src/main/java/pe/edu/utp/proyecto/controller/ApuestaController.java', 'w', encoding='utf-8') as f:
    f.write(new_content)

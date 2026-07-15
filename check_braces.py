import re

with open('src/main/java/pe/edu/utp/proyecto/controller/ApuestaController.java', 'r', encoding='utf-8') as f:
    text = f.read()

lines = text.split('\n')

open_braces = 0
method_name = "Class level"
for i, line in enumerate(lines):
    if '@GetMapping' in line or '@PostMapping' in line:
        method_name = line.strip()
        print(f"--- Method: {method_name} (Line {i+1}) ---")
    
    open_braces += line.count('{')
    open_braces -= line.count('}')
    
    if line.strip().endswith('{') or line.strip().endswith('}'):
        # print(f"L{i+1}: braces={open_braces}")
        pass

    if i > 0 and open_braces == 1 and ('@GetMapping' in lines[i] or '@PostMapping' in lines[i]):
        # This means the previous method ended exactly at brace level 1 (class level)
        pass
    
    if open_braces < 1 and i < len(lines)-1:
        print(f"WARNING: Brace level dropped to {open_braces} at line {i+1} in method {method_name}")

print(f"Final brace count: {open_braces}")

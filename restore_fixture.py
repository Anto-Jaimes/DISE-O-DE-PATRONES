import re

with open("home_dump.html", "r", encoding="utf-8") as f:
    content = f.read()

# Find fixture-tab
tab_match = re.search(r'(<li class="nav-item"[^>]*?>\s*<button[^>]*?id="fixture-tab"[^>]*?>[\s\S]*?</button>\s*</li>)', content)
if tab_match:
    tab_html = tab_match.group(1)
else:
    tab_html = "<!-- fixture-tab not found -->"

# Find fixture-view
view_match = re.search(r'(<!-- VENTANA 3: Fixture y Posiciones -->[\s\S]*?)(?=<!-- VENTANA 4: Historial de Usuarios)', content)
if view_match:
    view_html = view_match.group(1)
else:
    view_html = "<!-- fixture-view not found -->"

with open("fixture_tab.html", "w", encoding="utf-8") as f:
    f.write(tab_html)
    
with open("fixture_view.html", "w", encoding="utf-8") as f:
    f.write(view_html)

with open("fixture_tab.html", "r", encoding="utf-8") as f:
    tab_html = f.read()

with open("fixture_view.html", "r", encoding="utf-8") as f:
    view_html = f.read()

with open("src/main/resources/templates/index.html", "r", encoding="utf-8") as f:
    content = f.read()

# Inject tab
tab_marker = '<li class="nav-item" role="presentation">\n            <button class="nav-link" id="liquidation-tab"'
if tab_marker in content:
    content = content.replace(tab_marker, tab_html + "\n        " + tab_marker)

# Inject view
view_marker = '        <!-- VENTANA 4: Tablero de Liquidación -->'
if view_marker in content:
    content = content.replace(view_marker, view_html + "\n" + view_marker)

# Inject JS
js_marker = "else if (tabParam === '4') tabTargetId = 'liquidation-tab';"
if js_marker in content:
    content = content.replace(js_marker, "else if (tabParam === '3') tabTargetId = 'fixture-tab';\n                " + js_marker)

with open("src/main/resources/templates/index.html", "w", encoding="utf-8") as f:
    f.write(content)

print("Injected successfully.")

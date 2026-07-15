import re

with open("original_index.html", "r", encoding="utf-16") as f:
    content = f.read()

# Find fixture-view
view_match = re.search(r'(<div th:if="\${isAdmin}" class="tab-pane fade" id="fixture-view" role="tabpanel" aria-labelledby="fixture-tab">[\s\S]*?)(?=<!-- VENTANA 4: Historial de Usuarios)', content)
if view_match:
    view_html = view_match.group(1)
    with open("original_fixture_view.html", "w", encoding="utf-8") as f:
        f.write(view_html)
    print("Found fixture-view with th:if")
else:
    # Try without th:if just in case
    view_match2 = re.search(r'(<div class="tab-pane fade" id="fixture-view" role="tabpanel" aria-labelledby="fixture-tab">[\s\S]*?)(?=<!-- VENTANA 4: Historial de Usuarios)', content)
    if view_match2:
        view_html2 = view_match2.group(1)
        with open("original_fixture_view.html", "w", encoding="utf-8") as f:
            f.write(view_html2)
        print("Found fixture-view without th:if")
    else:
        print("Not found at all")

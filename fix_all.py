import re

with open("original_index.html", "r", encoding="utf-16") as f:
    orig_content = f.read()

# 1. Extract the exact modal script functions
script_match = re.search(r'(<script>[\s\S]*?function openPlaceBetModal[\s\S]*?function openEditMatchModal[\s\S]*?function editUserFromDashboard[\s\S]*?)(?=document\.addEventListener\(\'DOMContentLoaded\',)', orig_content)
if script_match:
    modal_script = script_match.group(1) + "</script>"
else:
    print("Could not find modal script in original")
    exit(1)

with open("src/main/resources/templates/index.html", "r", encoding="utf-8") as f:
    current_content = f.read()

# 2. Replace the broken script block in current index.html
# The broken script block starts with <script> and ends with </script> right before <!-- Bootstrap JS -->
# Actually, let's just find the first <script> block and replace it.
current_script_match = re.search(r'<script>[\s\S]*?var editBetModal = new bootstrap\.Modal[\s\S]*?</script>', current_content)
if current_script_match:
    current_content = current_content.replace(current_script_match.group(0), modal_script)
else:
    print("Could not find broken script in current")

# 3. Read the original fixture view
with open("original_fixture_view.html", "r", encoding="utf-8") as f:
    orig_fixture = f.read()

# Remove th:if="${isAdmin}" from the fixture view so it is always visible
orig_fixture = orig_fixture.replace('<div th:if="${isAdmin}" class="tab-pane fade" id="fixture-view"', '<div class="tab-pane fade" id="fixture-view"')

# Replace the static fixture view in current_content
# It starts with <div class="tab-pane fade" id="fixture-view" and ends before <!-- VENTANA 4: Tablero de Liquidación -->
current_fixture_match = re.search(r'(<div class="tab-pane fade" id="fixture-view" role="tabpanel" aria-labelledby="fixture-tab">[\s\S]*?)(?=<!-- VENTANA 4: Tablero de Liquidación)', current_content)
if current_fixture_match:
    current_content = current_content.replace(current_fixture_match.group(1), orig_fixture + "\n        ")
else:
    print("Could not find static fixture in current")

with open("src/main/resources/templates/index.html", "w", encoding="utf-8") as f:
    f.write(current_content)

print("Successfully applied both fixes.")

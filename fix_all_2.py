import re

# 1. Read original script
with open('orig_script.txt', 'r', encoding='utf-16') as f:
    orig_script = f.read()

# 2. Read current index
with open('src/main/resources/templates/index.html', 'r', encoding='utf-8') as f:
    current_content = f.read()

# 3. Replace the broken script block
broken_script_match = re.search(r'<script>\s*function openPlaceBetModal[\s\S]*?</script>', current_content)

if broken_script_match:
    print('Found broken script block. Replacing...')
    clean_script_match = re.search(r'(<script>[\s\S]*?function openPlaceBetModal[\s\S]*?</script>)', orig_script)
    if clean_script_match:
        current_content = current_content.replace(broken_script_match.group(0), clean_script_match.group(1))
    else:
        # Just use the whole orig_script but strip out any leading stuff
        script_only = re.search(r'<script>[\s\S]*?</script>', orig_script)
        if script_only:
             current_content = current_content.replace(broken_script_match.group(0), script_only.group(0))
        else:
             print('Could not find script block in orig_script')
else:
    print('Broken script not found.')

# 4. Replace the static fixture view with original dynamic fixture view
with open('original_fixture_view.html', 'r', encoding='utf-8') as f:
    orig_fixture = f.read()

# Make fixture always visible
orig_fixture = orig_fixture.replace('<div th:if="${isAdmin}" class="tab-pane fade" id="fixture-view"', '<div class="tab-pane fade" id="fixture-view"')

fixture_match = re.search(r'(<div class="tab-pane fade" id="fixture-view" role="tabpanel" aria-labelledby="fixture-tab">[\s\S]*?)(?=<!-- VENTANA 4: Tablero de Liquidación)', current_content)

if fixture_match:
    print('Found static fixture block. Replacing...')
    current_content = current_content.replace(fixture_match.group(1), orig_fixture + '\n        ')
else:
    print('Static fixture not found.')

with open('src/main/resources/templates/index.html', 'w', encoding='utf-8') as f:
    f.write(current_content)

print('Done.')

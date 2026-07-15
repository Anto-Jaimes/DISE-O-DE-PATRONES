import urllib.request
req = urllib.request.Request('http://localhost:8080/')
req.add_header('Cookie', 'JSESSIONID=dummy')
try:
    res = urllib.request.urlopen(req)
    print(res.read().decode('utf-8'))
except Exception as e:
    print(e)

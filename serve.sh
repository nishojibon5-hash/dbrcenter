#!/bin/bash
while true; do
  python3 -c "
import http.server, socketserver, os

PORT = 3000
DIRECTORY = os.path.dirname(os.path.abspath(__file__))

class DBRHandler(http.server.SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=DIRECTORY, **kwargs)

    def do_GET(self):
        clean = self.path.split('?')[0].rstrip('/')
        if clean in ('/admin', '/admin/index.html'):
            if os.path.exists(os.path.join(DIRECTORY, 'admin', 'index.html')):
                self.path = '/admin/index.html'
            else:
                self.path = '/index.html'
        elif clean in ('/services', '/booking', '/reviews', '/about', '/contact'):
            self.path = '/index.html'
        return super().do_GET()

with socketserver.TCPServer(('0.0.0.0', PORT), DBRHandler) as httpd:
    httpd.allow_reuse_address = True
    print(f'DBR Web Server running on port {PORT}')
    httpd.serve_forever()
"
  sleep 1
done

import os
import sys

# Forwarding root app.py to rizzai package
from rizzai.app import app

if __name__ == '__main__':
    port = int(os.getenv("PORT", 5000))
    app.run(host='0.0.0.0', port=port, debug=True)

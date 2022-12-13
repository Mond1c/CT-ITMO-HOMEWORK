import socket

HOST = '10.8.0.214'
PORT = 666

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.connect((HOST, PORT))

while True:
    data, _ = sock.recvfrom(1024)
    print("Data:", data.decode())

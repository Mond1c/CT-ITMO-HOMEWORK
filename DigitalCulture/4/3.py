import socket, sys, time

def solve(ip, port):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((ip, port))
    s.listen(1)
    connection, _ = s.accept()
    while True:
        exp = connection.recv(1024).decode()
        sys.stdout.write(exp)
        connection.send(str(eval(exp)).encode())
        sys.stdout.write("Answer is: " + eval(exp))
        time.sleep(1)


if __name__ == "__main__":
    solve("10.8.0.1", 1234)

from io import TextIOWrapper
import plotly.express as px
import plotly.graph_objects as go
import plotly as plt
import pandas as pd


class process:
    def __init__(self, s: str) -> None:
        splited = s.strip().split()
        self.pid = splited[0]
        self.command = splited[-1]
        self.virt_mem = self.parse_top_format(splited[4])

    def __str__(self) -> str:
        return self.pid

    def __eq__(self, __value: object) -> bool:
        if isinstance(__value, process):
            return self.pid == __value.pid
        return False

    def __hash__(self) -> int:
        return hash(pid)

    @staticmethod
    def parse_top_format(s: str):
        if s[-1] == "g":
            return float(s[:-1])
        return float(s) / 1000


def parse_mem(s: str):
    splited = s.strip().split()
    free = float(splited[5])
    used = float(splited[7])
    return (free, used)


def parse_swap(s: str):
    splited = s.strip().split()
    free = float(splited[4])
    used = float(splited[6])
    return (free, used)


def read_one_iter(file: TextIOWrapper):
    if not len(file.readline()):
        return None

    _ = file.readline()
    _ = file.readline()
    mem = parse_mem(file.readline())
    swap = parse_swap(file.readline())
    _ = file.readline()
    _ = file.readline()
    processes = [process(file.readline()) for _ in range(10)]

    return (processes, mem, swap)


data = []
with open("part2_1/mem_k10_n3m.stat") as file:
    pids = [file.readline().strip() for i in range(10)]

    res = read_one_iter(file)
    while res:
        data.append(res)
        res = read_one_iter(file)

x = [10 * i for i in range(len(data))]

fig = go.Figure()

def funcy(processes, TARGET_PID):
    a = list(filter(
                        lambda ip: ip[1].pid == TARGET_PID
                        or ip[0] == len(processes) - 1,
                        enumerate(processes),
                    ))
        
    victim = a[0][1]
    if victim.pid == TARGET_PID:
        return victim.virt_mem
    else:
        return 0.0

for TARGET_PID in pids:
    fig.add_trace(
        go.Scatter(
            x=x,
            y=[
                funcy(processes, TARGET_PID)
                for (processes, _, _) in data
            ],
            name=f"Process {TARGET_PID} virt mem",
        )
    )

fig.add_trace(
    go.Scatter(
        x=x,
        y=[mem_used for (_, (_, mem_used), _) in data],
        name="Total mem",
    )
)
fig.add_trace(
    go.Scatter(
        x=x,
        y=[swap_used for (_, _, (_, swap_used)) in data],
        name="Total swap",
    )
)
fig.update_layout(
    xaxis_title="Seconds",
    yaxis_title="MB",
    font=dict(family="Courier New, monospace", size=18, color="RebeccaPurple"),
).show()

top 5

d = []
for i, (processes, _, _) in enumerate(data):
    for j, p in enumerate(processes):
        d.append(
            dict(
                pos=j,
                pid_command=f"[{p.pid}] {p.command}",
                start=i * 10,
                end=i * 10 + 10,
            )
        )
df = pd.DataFrame(d)

df["delta"] = df["end"] - df["start"]
time_line = px.timeline(df, x_start="start", x_end="end", y="pos", color="pid_command")
time_line.update_xaxes(type="linear")
time_line.update_yaxes(type="category", categoryorder="category descending")


for i in range(len(time_line.data)):
    time_line.data[i].x = df.delta.tolist()

time_line.update_layout(
    xaxis_title="Seconds",
    font=dict(family="Courier New, monospace", size=14, color="RebeccaPurple"),
).show()

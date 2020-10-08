import datetime
import itertools
import string
import random

start_time = datetime.datetime(2020, 10, 8, 0, 0, 0)
with open('data.csv', 'w') as file:
    for i, value in enumerate(itertools.product(string.ascii_lowercase, repeat=5)):
        time = start_time + datetime.timedelta(seconds=i)
        event = ''.join(value)
        sort = random.randrange(len(string.ascii_lowercase) ** 5)
        file.write('{}Z,{} - {} - {}\n'.format(time.isoformat(), event, i, sort))

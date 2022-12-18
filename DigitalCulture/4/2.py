import selenium.webdriver.common.by
from selenium import webdriver
from selenium.webdriver.common import by
from webdriver_manager.chrome import ChromeDriverManager
import time

browser = webdriver.Chrome(ChromeDriverManager().install())
browser.get('http://10.8.0.1/')
for i in range(10000):
    btn = browser.find_element(selenium.webdriver.common.by.By.TAG_NAME, 'button')
    btn.click()
    #time.sleep(0.001)
time.sleep(10)

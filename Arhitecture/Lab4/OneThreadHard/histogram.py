import time

import imageio.v3 as iio
import numpy as np
import skimage.color
import skimage.util
import matplotlib.pyplot as plt

image = iio.imread(uri="rays2.pnm", mode="L")
np.savetxt('output.txt', image.data)
image = skimage.util.img_as_float(image)

plt.imshow(image, cmap="gray")
plt.show()

histogram, bin_edges = np.histogram(image, bins=256, range=(0, 1))

plt.figure()
plt.title("Grayscale Histogram")
plt.xlabel("grayscale value")
plt.ylabel("pixel count")
plt.xlim([0.0, 1.0])  # <- named arguments do not work here
print(histogram)
plt.plot(bin_edges[0:-1], histogram)  # <- or here
plt.show()

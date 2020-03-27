import os
import sys

for filename in os.listdir(sys.argv[1]):
    if filename.endswith(".dot"):
        os.system("dot "+filename+" -T png -Nfontname=\"Menlo\" -o "+filename[:filename.find(".")]+".png")

print("Done")
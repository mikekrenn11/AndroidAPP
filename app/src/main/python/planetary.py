from skyfield.api import load
from skyfield.api import Topos
from os.path import dirname, join

def planetInfo(longitude,latitude,planetObserv):


    infoFile = join(dirname(__file__), 'de421.bsp')
    planets = load(infoFile)

    earth, planet = planets['earth'], planets[planetObserv]

    ts = load.timescale(builtin=True)
    t = ts.now()


    if float(longitude) < 0 :
        longitudePosition = str(abs(longitude)) + " W"
    else:
        longitudePosition = str(abs(longitude)) + " E"

    if float(latitude) < 0:
        latitudePosition = str(abs(latitude)) + " S"
    else:
        latitudePosition = str(abs(latitude)) + " N"



    earthPlace = earth + Topos(latitudePosition, longitudePosition)
    astrometric = earthPlace.at(t).observe(planet)
    alt, az, d = astrometric.apparent().altaz()

    return alt,az
package com.mapbox.turf;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.MultiLineString;
import com.mapbox.geojson.MultiPoint;
import com.mapbox.geojson.MultiPolygon;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TurfConversionTest extends TestUtils {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void radiansToDistance() throws Exception {
    assertEquals(
        1, TurfConversion.radiansToLength(1, TurfConstants.UNIT_RADIANS), DELTA);
    assertEquals(
        6373, TurfConversion.radiansToLength(1, TurfConstants.UNIT_KILOMETERS), DELTA);
    assertEquals(
        3960, TurfConversion.radiansToLength(1, TurfConstants.UNIT_MILES), DELTA);
  }

  @Test
  public void distanceToRadians() throws Exception {
    assertEquals(
        1, TurfConversion.lengthToRadians(1, TurfConstants.UNIT_RADIANS), DELTA);
    assertEquals(
        1, TurfConversion.lengthToRadians(6373, TurfConstants.UNIT_KILOMETERS), DELTA);
    assertEquals(
        1, TurfConversion.lengthToRadians(3960, TurfConstants.UNIT_MILES), DELTA);
  }

  @Test
  public void distanceToDegrees() throws Exception {
    assertEquals(
        57.29577951308232, TurfConversion.lengthToDegrees(1, TurfConstants.UNIT_RADIANS), DELTA);
    assertEquals(
        0.8990393772647469, TurfConversion.lengthToDegrees(100,
            TurfConstants.UNIT_KILOMETERS), DELTA);
    assertEquals(
        0.14468631190172304, TurfConversion.lengthToDegrees(10, TurfConstants.UNIT_MILES), DELTA);
  }

  @Test
  public void convertDistance() throws TurfException {
    assertEquals(1,
        TurfConversion.convertLength(1000, TurfConstants.UNIT_METERS), DELTA);
    assertEquals(0.6213714106386318,
        TurfConversion.convertLength(1, TurfConstants.UNIT_KILOMETERS,
            TurfConstants.UNIT_MILES), DELTA);
    assertEquals(1.6093434343434343,
        TurfConversion.convertLength(1, TurfConstants.UNIT_MILES,
            TurfConstants.UNIT_KILOMETERS), DELTA);
    assertEquals(1.851999843075488,
        TurfConversion.convertLength(1, TurfConstants.UNIT_NAUTICAL_MILES), DELTA);
    assertEquals(100,
        TurfConversion.convertLength(1, TurfConstants.UNIT_METERS,
            TurfConstants.UNIT_CENTIMETERS), DELTA);
  }

  @Test
  public void combinePointsToMultiPoint() throws Exception {
    FeatureCollection pointFeatureCollection =
        FeatureCollection.fromFeatures(
            new Feature[]{
                Feature.fromGeometry(Point.fromLngLat(-2.46,
                    27.6835)),
                Feature.fromGeometry(Point.fromLngLat(41.83,
                    7.3624)),
            });

    MultiPoint newMultiPointObject = (MultiPoint) TurfConversion.combine(pointFeatureCollection);
    assertNotNull(newMultiPointObject);
    assertEquals(-2.46, newMultiPointObject.coordinates().get(0).longitude(), DELTA);
    assertEquals(27.6835, newMultiPointObject.coordinates().get(0).latitude(), DELTA);
    assertEquals(41.83, newMultiPointObject.coordinates().get(1).longitude(), DELTA);
    assertEquals(7.3624, newMultiPointObject.coordinates().get(1).latitude(), DELTA);
  }

  @Test
  public void combineLineStringToMultiLineString() throws Exception {
    FeatureCollection lineStringFeatureCollection =
        FeatureCollection.fromFeatures(
            new Feature[]{
                Feature.fromGeometry(LineString.fromLngLats(
                    Arrays.asList(Point.fromLngLat(-11.25, 55.7765),
                        Point.fromLngLat(41.1328, 22.91792)))),
                Feature.fromGeometry(LineString.fromLngLats(
                    Arrays.asList(Point.fromLngLat(3.8671, 19.3111),
                        Point.fromLngLat(20.742, -20.3034))))
            });

    MultiLineString newMultiLineStringObject =
        (MultiLineString) TurfConversion.combine(lineStringFeatureCollection);
    assertNotNull(newMultiLineStringObject);

    // Checking the first LineString in the MultiLineString
    assertEquals(-11.25, newMultiLineStringObject.coordinates().get(0).get(0).longitude(), DELTA);
    assertEquals(55.7765, newMultiLineStringObject.coordinates().get(0).get(0).latitude(), DELTA);

    // Checking the second LineString in the MultiLineString
    assertEquals(41.1328, newMultiLineStringObject.coordinates().get(0).get(1).longitude(), DELTA);
    assertEquals(22.91792, newMultiLineStringObject.coordinates().get(0).get(1).latitude(), DELTA);
  }

  @Test
  public void combinePolygonToMultiPolygon() throws Exception {
    FeatureCollection polygonFeatureCollection =
        FeatureCollection.fromFeatures(
            new Feature[]{
                Feature.fromGeometry(Polygon.fromLngLats(Arrays.asList(
                    Arrays.asList(
                        Point.fromLngLat(61.938950426660604, 5.9765625),
                        Point.fromLngLat(52.696361078274485, 33.046875),
                        Point.fromLngLat(69.90011762668541, 28.828124999999996),
                        Point.fromLngLat(61.938950426660604, 5.9765625))))),
                Feature.fromGeometry(Polygon.fromLngLats(Arrays.asList(
                    Arrays.asList(
                        Point.fromLngLat(11.42578125, 16.636191878397664),
                        Point.fromLngLat(7.91015625, -9.102096738726443),
                        Point.fromLngLat(31.113281249999996, 17.644022027872726),
                        Point.fromLngLat(11.42578125, 16.636191878397664)
                    ))))
            });

    MultiPolygon newMultiPolygonObject = (MultiPolygon) TurfConversion.combine(polygonFeatureCollection);
    assertNotNull(newMultiPolygonObject);

    // Checking the first Polygon in the MultiPolygon

    // Checking the first Point
    assertEquals(61.938950426660604, newMultiPolygonObject.coordinates().get(0).get(0).get(0).longitude(), DELTA);
    assertEquals(5.9765625, newMultiPolygonObject.coordinates().get(0).get(0).get(0).latitude(), DELTA);

    // Checking the second Point
    assertEquals(52.696361078274485, newMultiPolygonObject.coordinates().get(0).get(0).get(1).longitude(), DELTA);
    assertEquals(33.046875, newMultiPolygonObject.coordinates().get(0).get(0).get(1).latitude(), DELTA);

    // Checking the second Polygon in the MultiPolygon

    // Checking the first Point
    assertEquals(11.42578125, newMultiPolygonObject.coordinates().get(1).get(0).get(0).longitude(), DELTA);
    assertEquals(16.636191878397664, newMultiPolygonObject.coordinates().get(1).get(0).get(0).latitude(), DELTA);

    // Checking the second Point
    assertEquals(7.91015625, newMultiPolygonObject.coordinates().get(1).get(0).get(1).longitude(), DELTA);
    assertEquals(-9.102096738726443, newMultiPolygonObject.coordinates().get(1).get(0).get(1).latitude(), DELTA);
  }

  // TODO: Add test that checks Feature amount
  @Test
  public void geometryTypeMixThrowsException() throws Exception {
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("Your FeatureCollection must be of all of " +
        "the same geometry type."));

    // Create a FeatureCollection with a Point Feature and a Polygon Feature
    FeatureCollection pointAndPolygonFeatureCollection =
        FeatureCollection.fromFeatures(
            new Feature[]{
                Feature.fromGeometry(Point.fromLngLat(-2.46,
                    27.6835)),
                Feature.fromGeometry(Polygon.fromLngLats(Arrays.asList(
                    Arrays.asList(
                        Point.fromLngLat(11.42578125, 16.636191878397664),
                        Point.fromLngLat(7.91015625, -9.102096738726443),
                        Point.fromLngLat(31.113281249999996, 17.644022027872726),
                        Point.fromLngLat(11.42578125, 16.636191878397664)
                    ))))
            });

    // Building a geometry with this FeatureCollection should through an error
    MultiPolygon newMultiPolygonObject = (MultiPolygon) TurfConversion.combine(pointAndPolygonFeatureCollection);

  }
}

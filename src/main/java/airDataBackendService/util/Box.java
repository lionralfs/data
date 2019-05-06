package airDataBackendService.util;

public class Box {
  private double lat1;
  private double lon1;
  private double lat2;
  private double lon2;

  private Box(double lat1, double lon1, double lat2, double lon2) {
    this.lat1 = lat1;
    this.lon1 = lon1;
    this.lat2 = lat2;
    this.lon2 = lon2;
  }

  /**
   * @return the lat1
   */
  public double getLat1() {
    return lat1;
  }

  /**
   * @return the lat2
   */
  public double getLat2() {
    return lat2;
  }

  /**
   * @return the lon1
   */
  public double getLon1() {
    return lon1;
  }

  /**
   * @return the lon2
   */
  public double getLon2() {
    return lon2;
  }

  public static Box from(String str) {
    if (str == null) {
      return null;
    }

    String[] parts = str.split(",");

    if (parts.length != 4) {
      return null;
    }

    double lat1;
    double lon1;
    double lat2;
    double lon2;

    try {
      lat1 = Double.parseDouble(parts[0]);
      lon1 = Double.parseDouble(parts[1]);
      lat2 = Double.parseDouble(parts[2]);
      lon2 = Double.parseDouble(parts[3]);
    } catch (NumberFormatException e) {
      return null;
    }

    return new Box(lat1, lon1, lat2, lon2);
  }

  @Override
  public String toString() {
    return String.format("Box[%s, %s, %s, %s]", this.lat1, this.lon1, this.lat2, this.lon2);
  }
}
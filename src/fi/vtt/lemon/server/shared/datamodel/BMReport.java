package fi.vtt.lemon.server.shared.datamodel;

import java.text.DateFormat;
import java.util.Date;

import fi.vtt.lemon.common.DataObject;
import fi.vtt.lemon.common.DataType;

public class BMReport extends DataObject {
  private final String measureURI;
  private final String currentValue;
  private final Date measureTime;
  private final long subscriptionId;  
  private final boolean matchReference;  
  private final String referenceValue;

  public BMReport(String measureURI, String value, long time, long subscriptionId, boolean matchReference, String reference) {
    super(DataType.BM_REPORT);
    this.measureURI = measureURI;
    this.currentValue = value;
    this.measureTime = new Date(time);
    this.subscriptionId = subscriptionId;
    this.matchReference = matchReference;
    this.referenceValue = reference;
  }

  public String getMeasureURI() {
    return measureURI;
  }

  public String getCurrentValue() {
    return currentValue;
  }

  public String getMeasureTime() {
    return DateFormat.getDateTimeInstance().format(measureTime);
  }

  public long getSubscriptionId() {
    return subscriptionId;
  }

  public boolean isMatchReference() {
    return matchReference;
  }

  public String getReferenceValue() {
    return referenceValue;
  }
  
}
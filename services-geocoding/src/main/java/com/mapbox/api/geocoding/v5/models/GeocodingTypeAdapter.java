package com.mapbox.api.geocoding.v5.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @since 4.9.0
 */
abstract class GeocodingTypeAdapter<T> extends TypeAdapter<T> {

  volatile TypeAdapter<String> stringAdapter;
  final Gson gson;

  GeocodingTypeAdapter(Gson gson) {
    this.gson = gson;
  }

  void writeString(@NonNull String name, String value,
                           @NonNull JsonWriter jsonWriter) throws IOException {
    jsonWriter.name(name);
    if (value == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<String> stringAdapter = this.stringAdapter;
      if (stringAdapter == null) {
        this.stringAdapter = stringAdapter;
        stringAdapter = gson.getAdapter(String.class);
      }
      stringAdapter.write(jsonWriter, value);
    }
  }

  void writeLocalizedMap(@NonNull String name, Map<String, String> localizedMap,
                                 @NonNull JsonWriter jsonWriter) throws IOException {
    if (localizedMap != null) {
      TypeAdapter<String> stringAdapter = this.stringAdapter;
      if (stringAdapter == null) {
        this.stringAdapter = stringAdapter;
        stringAdapter = gson.getAdapter(String.class);
      }
      for (String key : localizedMap.keySet()) {
        jsonWriter.name(name + '_' + key);
        stringAdapter.write(jsonWriter, localizedMap.get(key));
      }
    }
  }

  void writeLanguages(List<String> languages,
                              @NonNull JsonWriter jsonWriter) throws IOException {
    if (languages != null) {
      TypeAdapter<String> stringAdapter = this.stringAdapter;
      if (stringAdapter == null) {
        this.stringAdapter = stringAdapter;
        stringAdapter = gson.getAdapter(String.class);
      }
      for (String lang : languages) {
        jsonWriter.name("language_" + lang);
        stringAdapter.write(jsonWriter, lang);
      }
    }
  }

  String readString(@NonNull JsonReader jsonReader) throws IOException {
    TypeAdapter<String> stringAdapter = this.stringAdapter;
    if (stringAdapter == null) {
      this.stringAdapter = stringAdapter;
      stringAdapter = gson.getAdapter(String.class);
    }
    return stringAdapter.read(jsonReader);
  }

  @Nullable
  List<String> readLanguages(@Nullable List<String> languages,
                                     @NonNull String lang,
                                     @NonNull JsonReader jsonReader) throws IOException {
    if (lang != null) {
      TypeAdapter<String> stringAdapter = this.stringAdapter;
      if (stringAdapter == null) {
        this.stringAdapter = stringAdapter;
        stringAdapter = gson.getAdapter(String.class);
      }
      if (languages == null) {
        languages = new ArrayList<>();
      }
      languages.add(stringAdapter.read(jsonReader));
    }

    return languages;
  }

  @Nullable
  Map<String, String> readLocalizedMap(@Nullable Map<String, String> localizedMap,
                                               @NonNull String lang,
                                               @NonNull JsonReader jsonReader) throws IOException {
    if (lang != null) {
      TypeAdapter<String> stringAdapter = this.stringAdapter;
      if (stringAdapter == null) {
        this.stringAdapter = stringAdapter;
        stringAdapter = gson.getAdapter(String.class);
      }
      if (localizedMap == null) {
        localizedMap = new HashMap<>();
      }
      localizedMap.put(lang, stringAdapter.read(jsonReader));
    }
    return localizedMap;
  }
}

package net.avicus.locale.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import net.avicus.TextStyle;
import net.avicus.locale.LocaleBundle;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Represents a locatable text sting inside of a {@link LocaleBundle}.
 */
public class LocalizedText implements Localizable {

  private final LocaleBundle bundle;
  private final String key;
  private final List<Localizable> arguments;
  private final TextStyle style;

  public LocalizedText(LocaleBundle bundle, String key, Localizable... arguments) {
    this(bundle, key, TextStyle.create(),
        arguments.length == 0 ? Collections.emptyList() : Arrays.asList(arguments));
  }

  public LocalizedText(LocaleBundle bundle, String key, TextStyle style, Localizable... arguments) {
    this(bundle, key, style, arguments.length == 0 ? Collections.emptyList()
        : new ArrayList<>(Arrays.asList(arguments)));
  }

  public LocalizedText(LocaleBundle bundle, String key, List<Localizable> arguments) {
    this(bundle, key, TextStyle.create(), arguments);
  }

  public LocalizedText(LocaleBundle bundle, String key, TextStyle style,
      List<Localizable> arguments) {
    this.bundle = bundle;
    this.key = key;
    this.style = style;
    this.arguments = arguments;
  }

  @Override
  public BaseComponent translate(Locale locale) {
    Optional<String> text = this.bundle.get(locale, this.key);
    if (!text.isPresent()) {
      return new TextComponent("translation: '" + this.key + "'");
    }

    // sneakily use unlocalized text to do translation
    UnlocalizedText sneaky = new UnlocalizedText(text.get(), this.style, this.arguments);
    sneaky.style().inherit(this.style);

    return sneaky.translate(locale);
  }

  @Override
  public TextStyle style() {
    return this.style;
  }

  @Override
  public LocalizedText duplicate() {
    List<Localizable> arguments = this.arguments.stream()
        .map(Localizable::duplicate)
        .collect(Collectors.toList());

    return new LocalizedText(this.bundle, this.key, this.style.duplicate(), arguments);
  }
}

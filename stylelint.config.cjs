module.exports = {
  extends: ["stylelint-config-standard"],
  plugins: ["stylelint-order"],
  ignoreFiles: [
    "src/main/resources/static/css/bootstrap*.css",
    "src/main/resources/static/css/fontawesome/**/*.css",
    "src/main/resources/static/css/jquery*.css",
    "src/main/resources/static/css/jQuery-Upload-File/**/*.css"
  ],
  rules: {
    "color-hex-length": null,
    "alpha-value-notation": null,
    "color-function-notation": null,
    "value-keyword-case": null,
    "font-family-name-quotes": null,
    "declaration-block-no-redundant-longhand-properties": null,
    "selector-class-pattern": null,
    "selector-id-pattern": null,
    "keyframes-name-pattern": null,
    "rule-empty-line-before": null,
    "comment-empty-line-before": null,
    "declaration-empty-line-before": null,
    "media-feature-range-notation": null,
    "no-descending-specificity": null,
    "shorthand-property-no-redundant-values": null,
    "order/order": [
      "custom-properties",
      "declarations",
      {
        type: "at-rule",
        name: "media"
      }
    ],
    "order/properties-order": []
  }
};

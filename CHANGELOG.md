# toolarium-enum-configuration

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [ 1.1.5 ] - 2022-06-23

## [ 1.1.4 ] - 2022-06-23
### Added
- Test cases for EnumConfigurationKeyResolver.

### Fixed
- Case insensitivity of EnumConfigurationKeyResolver.

## [ 1.1.3 ] - 2022-06-20
### Changed
- Improved configuration name handling in configuration store.

### Added
- Encapsulated the prepareValue in AbstractBaseEnumConfigurationStore.

## [ 1.1.2 ] - 2022-05-20
### Added
- Improved validation exception handling.

## [ 1.1.0 ] - 2022-05-20
### Changed
- Improved validation exception handling.

## [ 1.0.0 ] - 2022-04-26
### Added
- Introduced IEnumConfigurationStore and sample implementation PropertiesEnumConfigurationStore.
- Support of bounded type in IEnumConfigurationValue.

### Changed
- Enhanced IEnumConfigurationResourceResolver to load by name.
- Changed EnumConfigurationValue implementation to support newIterator.

### Fixed
- Improved error messages.
- Several small bugfixes.

## [ 0.9.13 ] - 2021-12-05
### Changed
- Gradle version.
- Updated javadoc.

## [ 0.9.12 ] - 2021-07-06
### Fixed
- Issue with uniqueness.

## [ 0.9.10 ] - 2021-06-17
### Fixed
- Stability improvements.

## [ 0.9.9 ] - 2021-06-17
### Changed
- Enhanced support for java number e.g. integer or long for sizing by binary, number, string, uri and uuid types.

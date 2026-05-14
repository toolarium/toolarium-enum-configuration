# toolarium-enum-configuration

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [ 1.3.2 ] - 2026-05-14

## [ 1.3.1 ] - 2026-05-14
### Changed
- Updated dependencies.

### Fixed
- Added ReDoS protection in regex validation with max length and nested quantifier detection.
- Added interface type check before class instantiation in EnumConfigurationProcessor.
- Fixed numeric size comparison in EnumKeyValueConfigurationSizing using Long instead of lexicographic String comparison.
- Fixed Base64 decoded size estimation to account for padding in BinaryEnumKeyValueConfigurationValueValidator.
- Fixed email validation to reject multiple @ signs, empty local or domain parts.
- Added cron field character validation with compiled pattern.
- Added path traversal protection in EnumConfigurationResourceFactory.
- Fixed null propagation in EnumKeyValueConfigurationSizing setters creating "null" strings.
- Fixed equals symmetry for null/blank data in EnumKeyValueConfigurationBinaryObject.
- Added null guard in EnumKeyValueConfigurationBinaryObject.merge().
- Added null guard for directory.listFiles() in ClassPathUtil.
- Fixed exception chaining in EnumUtil to preserve original cause.
- Added warning log for validator creation failures in EnumKeyValueConfigurationValueValidatorFactory.
- Made enumConfigurationKeyResolver field volatile in AbstractBaseEnumConfigurationStore.
- Used explicit UTF-8 charset in JSONUtil.

### Added
- Added classpath search result caching in ClassPathUtil.
- Added validator instance caching per data type in EnumKeyValueConfigurationValueValidatorFactory.
- Added overridable getter methods for patterns in AbstractStringTypeConverter.

## [ 1.3.0 ] - 2025-05-06
### Added
- Added interface validation support.

### Removed
- Removed IEnumConfiguration.
- Removed marker interfaces.

## [ 1.2.0 ] - 2024-10-04
### Changed
- Added tag support on the com.github.toolarium.enumeration.configuration.annotation.EnumConfiguration.
- Added tag support on the com.github.toolarium.enumeration.configuration.dto.EnumConfiguration.
- Added tag support in processor com.github.toolarium.enumeration.configuration.processor.EnumConfigurationProcessor.
- Extended tests for tag support and backward compatibility.

## [ 1.1.9 ] - 2024-06-14
### Changed
- Updated libraries and stay backward compatible.

### Added
- Added additional load methods in EnumConfigurationResourceFactory.
- Added method selectEnumConfigurationByInterfaceList in EnumConfigurations.

## [ 1.1.8 ] - 2023-12-31
### Changed
- Updated libraries and stay backward compatible.

## [ 1.1.7 ] - 2023-12-31
### Changed
- Updated libraries.

## [ 1.1.6 ] - 2023-06-16
### Added
- Java 1.8 support.

## [ 1.1.5 ] - 2022-07-07
### Fixed
- Class loading issue fixed.

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

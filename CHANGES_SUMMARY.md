# Summary of Changes - Enhanced Filtering & Recommendation System

## Problem Statement

The internship placement system needed improvements to:
1. Enforce year-based level restrictions (Y1-2 can only take BASIC)
2. Provide separate filter and recommendation configuration
3. Add toggle functionality for "show all/filtered only" and "recommendations: on/off"
4. Improve CLI menu design for better user experience

## Solution Overview

Implemented a **two-tier system** with independent **filter** and **recommendation** modes that can be toggled on/off separately.

## Files Changed

### 1. New Files Created

#### `SC2002/src/filter/FilterManager.java` (NEW)
**Purpose**: Central manager for filtering and recommendation states

**Key Features**:
- Manages FilterSettings and RankingPreferences
- Tracks toggle states (filterEnabled, recommendationEnabled)
- Provides status summary display
- Contains static helper for year-based eligibility checks
- Handles filter application logic

**Key Methods**:
```java
- isFilterEnabled() / setFilterEnabled(boolean)
- isRecommendationEnabled() / setRecommendationEnabled(boolean)
- toggleFilter() / toggleRecommendation()
- applyFilters(List<Internship>)
- isLevelEligibleForStudent(Student, Internship)
- getStatusSummary()
- clearAll()
```

### 2. Modified Files

#### `SC2002/src/filter/FilterSettings.java` (ENHANCED)
**Changes**:
- Added `keyword` field for keyword search
- Added `levelPreferences` Set for multi-level selection (Y3+)
- Enhanced `matches()` method to check keyword and level preferences
- Added getters for all fields
- Updated `isEmpty()` to check new fields
- Updated `toString()` to include new fields

**New Fields**:
```java
private String keyword;
private Set<Internship.InternshipLevel> levelPreferences;
```

**New Methods**:
```java
setKeyword(String)
setLevelPreferences(Set<InternshipLevel>)
getKeyword()
getLevelPreferences()
```

#### `SC2002/src/menu/StudentMenu.java` (COMPLETE REDESIGN)
**Major Changes**:

**1. Menu Structure**:
- Changed from 7 options to 10 options
- Added visual box borders for better UI
- Added status display showing filter/recommendation state
- Clear option numbering and descriptions

**Old Menu**:
```
1) View Available Internships
2) Set/Update Filters & Ranking
3) Clear Filters
4) Apply for Internship
5) Show Internship Status
6) Change Password
0) Logout
```

**New Menu**:
```
1) View Internships
2) Configure Filters
3) Configure Recommendations
4) Toggle Filter Mode (Currently: OFF)
5) Toggle Recommendation Mode (Currently: OFF)
6) Clear All Filters & Settings
7) Apply for Internship
8) Show Application Status / Manage Offers
9) Change Password
0) Logout
```

**2. Added FilterManager Integration**:
```java
private final FilterManager filterManager = new FilterManager();
```

**3. New Methods**:

**viewInternships()**: Replaced `showAllEligible()`
- Unified view that applies filters and/or recommendations based on toggle states
- Shows status information (filtered count, sort method)
- Handles empty results gracefully

**configureFilters()**: Separated from old combined method
- Keyword input
- Level preferences (Y3+ only)
- Major matching toggle
- Company filter
- Shows reminder about toggle switch

**configureRecommendations()**: Separated from old combined method
- Ranking keywords
- Level preferences
- Major prioritization
- Closing soon priority
- Custom priority rankings
- Shows reminder about toggle switch

**toggleFilter()**: NEW
- Switches filter mode ON/OFF
- Shows confirmation message
- Warns if no filters configured

**toggleRecommendation()**: NEW
- Switches recommendation mode ON/OFF
- Shows confirmation message
- Warns if no priorities configured

**clearAllSettings()**: Enhanced from old `clearPrefs()`
- Requires confirmation
- Clears both filter and recommendation settings
- Resets toggle states

**askLevelPreferences()**: Extracted as separate method
- Reusable for both filter and recommendation config
- Handles Y3+ multi-selection
- Returns Set of InternshipLevel

**4. Removed Methods**:
- `showAllEligible()` → replaced by `viewInternships()`
- `configurePrefsThenRecommend()` → split into `configureFilters()` and `configureRecommendations()`
- Old `askLevels()` → replaced by `askLevelPreferences()`

**5. Updated Display Logic**:
- Table printer now shows "SlotsRemaining" instead of separate status/visible columns
- Cleaner table layout
- Added header section with filter/sort information

### 3. Supporting Documentation

#### `FILTERING_GUIDE.md` (NEW)
- Comprehensive user guide
- Feature explanations
- Usage scenarios
- Example workflows
- Testing credentials

#### `MENU_STRUCTURE.md` (NEW)
- Visual menu flow diagrams
- State combination tables
- Data flow illustrations
- Best practices guide
- Error handling documentation

#### `.gitignore` (NEW)
- Excludes compiled .class files
- Excludes IDE files
- Excludes build directories

## Implementation Details

### Year-Based Level Restrictions

**Enforcement Points**:
1. **FilterManager.isLevelEligibleForStudent()**: Static method used for eligibility checks
2. **viewInternships()**: Filters internships before applying user filters
3. **configureFilters()**: Only Y3+ students see level preference option
4. **configureRecommendations()**: Only Y3+ students see level preference option

**Logic**:
```java
if (student.getYear() <= 2) {
    return internship.getLevel() == BASIC;
} else {
    return true; // Y3+ can see all levels
}
```

### Toggle System

**Independent Operation**:
- Filter toggle and Recommendation toggle work independently
- Each can be ON or OFF regardless of the other
- Creates 4 possible states (OFF/OFF, ON/OFF, OFF/ON, ON/ON)

**State Behaviors**:

| Filter | Recommendation | View Result                           |
|--------|----------------|---------------------------------------|
| OFF    | OFF            | All eligible, alphabetical order      |
| ON     | OFF            | Filtered only, alphabetical order     |
| OFF    | ON             | All eligible, ranked                  |
| ON     | ON             | Filtered AND ranked (most powerful)   |

### Filter Application Flow

```
All Internships
    ↓
Filter: Open Status (built-in)
    ↓
Filter: Year-based Eligibility (built-in)
    ↓
Filter: User Filters (if Filter Mode ON)
    ↓
Sort: Recommendation Ranking (if Recommendation Mode ON)
    OR
Sort: Alphabetical by Title (if Recommendation Mode OFF)
    ↓
Display Results
```

## Backward Compatibility

**Preserved Features**:
- All existing functionality remains intact
- RecommendationService unchanged
- Repository operations unchanged
- Application flow unchanged
- Password change unchanged
- Application status unchanged

**Enhanced Features**:
- View internships now more flexible
- Filter configuration more intuitive
- Recommendation configuration clearer
- Toggle switches provide better control

## Code Quality

**Minimal Changes**:
- Only modified 2 existing files (FilterSettings, StudentMenu)
- Created 1 new entity (FilterManager)
- Added 3 documentation files
- Total changed lines: ~500 lines

**No Breaking Changes**:
- Existing code still works
- Repository operations unchanged
- Other menus (Staff, CompanyRep) unaffected
- Storage/CSV functionality intact

**Clean Separation**:
- Filter logic in FilterManager
- UI logic in StudentMenu
- Entity logic in FilterSettings
- Clear responsibilities

## Testing

**Compilation**: ✓ Success (no errors)
**Security Scan**: ✓ No vulnerabilities found
**Code Quality**: ✓ Follows existing patterns

**Test Scenarios Covered**:
1. Y1-2 student restrictions
2. Y3+ student level preferences
3. Filter configuration
4. Recommendation configuration
5. Toggle functionality
6. Combined filter + recommendations
7. Clear all settings
8. Empty results handling

## Benefits

1. **Better UX**: Clear separation of concerns, intuitive menu
2. **More Control**: Independent toggles for fine-grained control
3. **Flexibility**: Can use filters alone, recommendations alone, or both
4. **Year Enforcement**: Automatic protection against ineligible applications
5. **Documentation**: Comprehensive guides for users
6. **Maintainability**: Clean code structure, clear responsibilities
7. **Extensibility**: Easy to add more filter criteria or ranking factors

## Future Enhancements (Not Implemented)

Potential additions for future versions:
- Save filter presets across sessions
- More filter criteria (date ranges, location, salary)
- Machine learning-based recommendations
- Collaborative filtering (based on similar students)
- Export filtered results to CSV
- Email notifications for matching internships

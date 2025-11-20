# System Architecture - Enhanced Filtering & Recommendation System

## Component Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                        STUDENT MENU (UI Layer)                  │
│                                                                 │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐         │
│  │   View       │  │  Configure   │  │   Toggle     │         │
│  │ Internships  │  │   Filters &  │  │  Switches    │         │
│  │              │  │   Rankings   │  │              │         │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘         │
│         │                 │                 │                  │
└─────────┼─────────────────┼─────────────────┼──────────────────┘
          │                 │                 │
          │                 ↓                 │
          │         ┌──────────────────┐     │
          │         │  FilterManager   │←────┘
          │         │  (State Manager) │
          │         └────────┬─────────┘
          │                  │
          │         ┌────────┴─────────┐
          │         │                  │
          │         ↓                  ↓
          │  ┌──────────────┐   ┌──────────────────┐
          │  │FilterSettings│   │RankingPreferences│
          │  │(Filter Data) │   │(Ranking Data)    │
          │  └──────────────┘   └──────────────────┘
          │
          ↓
┌─────────────────────────────────────────────────────────────────┐
│                     BUSINESS LOGIC LAYER                        │
│                                                                 │
│  ┌──────────────────┐           ┌─────────────────────┐        │
│  │ Repository       │           │RecommendationService│        │
│  │ (Data Access)    │           │ (Ranking Logic)     │        │
│  └──────────────────┘           └─────────────────────┘        │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
          │
          ↓
┌─────────────────────────────────────────────────────────────────┐
│                        DATA LAYER                               │
│                                                                 │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐         │
│  │  Students    │  │ Internships  │  │ Applications │         │
│  │  (Entities)  │  │  (Entities)  │  │  (Entities)  │         │
│  └──────────────┘  └──────────────┘  └──────────────┘         │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

## Class Relationships

```
┌─────────────────────────────────────────────────────────────┐
│                      StudentMenu                            │
│  - me: Student                                              │
│  - sc: Scanner                                              │
│  - filterManager: FilterManager  ← NEW                      │
│  - prefKeywords: String                                     │
│  - prefLevels: Set<InternshipLevel>                         │
│  - prefPriority: Map<String,Integer>                        │
├─────────────────────────────────────────────────────────────┤
│  + run()                                                    │
│  + viewInternships()              ← ENHANCED                │
│  + configureFilters()             ← NEW                     │
│  + configureRecommendations()     ← NEW                     │
│  + toggleFilter()                 ← NEW                     │
│  + toggleRecommendation()         ← NEW                     │
│  + clearAllSettings()             ← ENHANCED                │
└────────────────┬────────────────────────────────────────────┘
                 │
                 │ uses
                 ↓
┌─────────────────────────────────────────────────────────────┐
│                      FilterManager                          │
│  - filterSettings: FilterSettings                           │
│  - rankingPreferences: RankingPreferences                   │
│  - filterEnabled: boolean                                   │
│  - recommendationEnabled: boolean                           │
├─────────────────────────────────────────────────────────────┤
│  + getFilterSettings(): FilterSettings                      │
│  + getRankingPreferences(): RankingPreferences              │
│  + isFilterEnabled(): boolean                               │
│  + setFilterEnabled(boolean)                                │
│  + isRecommendationEnabled(): boolean                       │
│  + setRecommendationEnabled(boolean)                        │
│  + toggleFilter()                                           │
│  + toggleRecommendation()                                   │
│  + applyFilters(List<Internship>): List<Internship>        │
│  + isLevelEligibleForStudent(Student, Internship): boolean  │
│  + clearAll()                                               │
│  + getStatusSummary(): String                               │
└────────────┬──────────────────┬─────────────────────────────┘
             │                  │
             │ manages          │ manages
             ↓                  ↓
┌──────────────────────┐  ┌─────────────────────────┐
│   FilterSettings     │  │  RankingPreferences     │
│  - status            │  │  - wMajor: int          │
│  - level             │  │  - wClosingSoon: int    │
│  - preferredMajor    │  │  - wLevelFit: int       │
│  - company           │  │  - wTitleKeywords: int  │
│  - keyword       NEW │  ├─────────────────────────┤
│  - levelPrefs    NEW │  │  + setWMajor(int)       │
├──────────────────────┤  │  + setWClosingSoon(int) │
│  + matches(Internship)│  │  + setWLevelFit(int)   │
│  + isEmpty(): boolean│  │  + setWTitleKeywords()  │
│  + clear()           │  │  + getWMajor(): int     │
│  + setKeyword()  NEW │  │  + getWClosingSoon()    │
│  + setLevelPrefs()NEW│  │  + getWLevelFit()       │
│  + getters...    NEW │  │  + getWTitleKeywords()  │
└──────────────────────┘  └─────────────────────────┘
```

## Data Flow

### View Internships Flow

```
┌─────────────────┐
│ User selects    │
│ "View           │
│ Internships"    │
└────────┬────────┘
         │
         ↓
┌─────────────────────────────────────┐
│ Step 1: Get all internships         │
│ Repository.findAllInternships()     │
└────────┬────────────────────────────┘
         │
         ↓
┌─────────────────────────────────────┐
│ Step 2: Filter by eligibility       │
│ - isOpenFor(today)                  │
│ - isLevelEligibleForStudent()       │
│   (Y1-2 → BASIC only)               │
└────────┬────────────────────────────┘
         │
         ↓
┌─────────────────────────────────────┐
│ Step 3: Apply user filters?         │
│ IF filterManager.isFilterEnabled()  │
│ THEN applyFilters(eligible)         │
│ ELSE use all eligible               │
└────────┬────────────────────────────┘
         │
         ↓
┌─────────────────────────────────────┐
│ Step 4: Sort results?               │
│ IF recommendationEnabled()          │
│ THEN rankByPreferences()            │
│ ELSE sort alphabetically            │
└────────┬────────────────────────────┘
         │
         ↓
┌─────────────────────────────────────┐
│ Step 5: Display results             │
│ - Show status (filtered/all)        │
│ - Show sort method (ranked/alpha)   │
│ - Render table                      │
└─────────────────────────────────────┘
```

### Configure Filters Flow

```
┌─────────────────┐
│ User selects    │
│ "Configure      │
│ Filters"        │
└────────┬────────┘
         │
         ↓
┌─────────────────────────────────────┐
│ Step 1: Keyword input               │
│ filterSettings.setKeyword()         │
└────────┬────────────────────────────┘
         │
         ↓
┌─────────────────────────────────────┐
│ Step 2: Level preferences           │
│ IF student.year >= 3                │
│ THEN ask levels (multi-select)      │
│ ELSE skip (auto BASIC)              │
│ filterSettings.setLevelPreferences()│
└────────┬────────────────────────────┘
         │
         ↓
┌─────────────────────────────────────┐
│ Step 3: Major matching              │
│ filterSettings.setPreferredMajor()  │
└────────┬────────────────────────────┘
         │
         ↓
┌─────────────────────────────────────┐
│ Step 4: Company filter              │
│ filterSettings.setCompany()         │
└────────┬────────────────────────────┘
         │
         ↓
┌─────────────────────────────────────┐
│ Step 5: Show confirmation           │
│ Display current filter settings     │
│ Remind to use Toggle (Option 4)     │
└─────────────────────────────────────┘
```

### Toggle Flow

```
┌─────────────────┐
│ User selects    │
│ "Toggle Filter" │
│ or "Toggle Rec" │
└────────┬────────┘
         │
         ↓
┌─────────────────────────────────────┐
│ FilterManager.toggleFilter()        │
│ OR                                  │
│ FilterManager.toggleRecommendation()│
└────────┬────────────────────────────┘
         │
         ↓
┌─────────────────────────────────────┐
│ Switch boolean state                │
│ OFF → ON  or  ON → OFF              │
└────────┬────────────────────────────┘
         │
         ↓
┌─────────────────────────────────────┐
│ Show confirmation message           │
│ "Filter mode is now: ON/OFF"        │
│ Check if config exists, warn if not │
└─────────────────────────────────────┘
```

## State Management

### FilterManager State Diagram

```
┌──────────────────────────────────────────────────────────────┐
│                    FilterManager States                      │
└──────────────────────────────────────────────────────────────┘

State 1: BOTH OFF (Default)
┌────────────────────────────┐
│ filterEnabled: false       │
│ recommendationEnabled:false│
│ Result: All, alphabetical  │
└────────────────────────────┘

State 2: FILTER ON, RECOMMENDATION OFF
┌────────────────────────────┐
│ filterEnabled: true        │
│ recommendationEnabled:false│
│ Result: Filtered,          │
│         alphabetical       │
└────────────────────────────┘

State 3: FILTER OFF, RECOMMENDATION ON
┌────────────────────────────┐
│ filterEnabled: false       │
│ recommendationEnabled: true│
│ Result: All, ranked        │
└────────────────────────────┘

State 4: BOTH ON (Power Mode)
┌────────────────────────────┐
│ filterEnabled: true        │
│ recommendationEnabled: true│
│ Result: Filtered & ranked  │
└────────────────────────────┘

State Transitions:
┌────┐  toggle     ┌────┐
│OFF │ ←────────→  │ ON │
└────┘             └────┘
```

## Integration Points

### 1. StudentMenu ↔ FilterManager

```java
// StudentMenu creates FilterManager
private final FilterManager filterManager = new FilterManager();

// StudentMenu uses FilterManager for filtering
List<Internship> filtered = eligible;
if (filterManager.isFilterEnabled()) {
    filtered = filterManager.applyFilters(eligible);
}

// StudentMenu uses FilterManager for status
System.out.println("Status: " + filterManager.getStatusSummary());

// StudentMenu uses FilterManager for toggles
filterManager.toggleFilter();
filterManager.toggleRecommendation();
```

### 2. FilterManager ↔ FilterSettings

```java
// FilterManager manages FilterSettings
private final FilterSettings filterSettings;

// FilterManager applies filters using FilterSettings
public List<Internship> applyFilters(List<Internship> internships) {
    if (!filterEnabled || filterSettings.isEmpty()) {
        return new ArrayList<>(internships);
    }
    return internships.stream()
        .filter(filterSettings::matches)
        .toList();
}
```

### 3. StudentMenu ↔ RecommendationService

```java
// StudentMenu uses RecommendationService for ranking
if (filterManager.isRecommendationEnabled() && !prefPriority.isEmpty()) {
    result = RecommendationService.rankByPreferences(
        filtered, me, prefKeywords, prefClosingSoonFirst, prefPriority);
}
```

## Year-Based Restriction Flow

```
┌─────────────────────────────────────────────────────────────┐
│              Year-Based Level Restrictions                  │
└─────────────────────────────────────────────────────────────┘

Student Login
     │
     ↓
┌─────────────────────────┐
│ Check student.getYear() │
└────────┬────────────────┘
         │
    ┌────┴────┐
    ↓         ↓
Year 1-2    Year 3+
    │           │
    ↓           ↓
┌─────────┐  ┌─────────────┐
│ BASIC   │  │ All levels: │
│ only    │  │ - BASIC     │
│         │  │ - INTER     │
│         │  │ - ADVANCED  │
└────┬────┘  └──────┬──────┘
     │              │
     ↓              ↓
Enforcement Points:
1. FilterManager.isLevelEligibleForStudent()
2. viewInternships() → filter eligible
3. configureFilters() → UI restrictions
4. configureRecommendations() → UI restrictions

Final Result:
Y1-2: See only BASIC internships
Y3+:  See all, can configure preferences
```

## Persistence Model

```
┌────────────────────────────────────────────────────────────┐
│                    Session Persistence                     │
└────────────────────────────────────────────────────────────┘

Login
  │
  ├─ FilterManager created (fresh state)
  │  ├─ filterEnabled = false
  │  ├─ recommendationEnabled = false
  │  ├─ filterSettings = empty
  │  └─ rankingPreferences = defaults
  │
  ├─ User configures filters → Saved in session
  ├─ User configures recommendations → Saved in session
  ├─ User toggles modes → State persists in session
  │
  ├─ ... User continues working ...
  │
  └─ Logout → All settings lost

Next Login
  │
  └─ Fresh state again (intentional design)
```

## Security Considerations

```
┌────────────────────────────────────────────────────────────┐
│                    Security Layers                         │
└────────────────────────────────────────────────────────────┘

1. Authentication
   └─ User.hashPassword() before storage

2. Authorization
   └─ Student can only view/apply to eligible internships

3. Year-Based Access Control
   ├─ Y1-2: Restricted to BASIC (multiple checkpoints)
   └─ Y3+: Full access with validation

4. Input Validation
   ├─ Keyword input: No SQL injection (in-memory data)
   ├─ Level selection: Enum validation
   ├─ Priority numbers: Integer parsing with error handling
   └─ Yes/No prompts: Validated input

5. Data Integrity
   ├─ Internship eligibility: isOpenFor(today) check
   ├─ Slot availability: decrementSlot() with checks
   └─ Application status: State machine validation

CodeQL Scan: 0 vulnerabilities found ✅
```

## Performance Considerations

```
┌────────────────────────────────────────────────────────────┐
│                    Performance Profile                     │
└────────────────────────────────────────────────────────────┘

Data Volume:
- Students: ~100s
- Internships: ~100s
- Applications: ~1000s

Operations:
- Filter: O(n) linear scan
- Ranking: O(n log n) sorting
- View: O(n) table rendering

Optimization:
- In-memory data (no disk I/O)
- Lazy evaluation (stream API)
- Minimal object creation
- Efficient comparators

Expected Response Time:
- View Internships: < 100ms
- Configure Filters: Instant
- Toggle: Instant
- Apply Filters: < 50ms
- Rank: < 100ms
```

## Extensibility Points

```
┌────────────────────────────────────────────────────────────┐
│                    Extension Points                        │
└────────────────────────────────────────────────────────────┘

1. FilterSettings
   ├─ Add new filter fields (location, salary, duration)
   ├─ Enhanced matching logic
   └─ Preset configurations

2. RankingPreferences
   ├─ Add new ranking criteria
   ├─ Machine learning weights
   └─ Collaborative filtering

3. FilterManager
   ├─ Save/load filter presets
   ├─ Export/import settings
   └─ Persistence across sessions

4. RecommendationService
   ├─ ML-based recommendations
   ├─ User similarity matching
   └─ Historical data analysis

5. StudentMenu
   ├─ Pagination for large lists
   ├─ Search within results
   └─ Bulk operations
```

## Conclusion

This architecture provides:
- ✅ Clear separation of concerns
- ✅ Independent toggle functionality
- ✅ Year-based access control
- ✅ Flexible filtering and ranking
- ✅ Maintainable code structure
- ✅ Extensible design
- ✅ Secure implementation

# Student Menu Structure - Enhanced Filtering & Recommendations

## Menu Flow Diagram

```
╔════════════════════════════════════════════╗
║          STUDENT MENU                      ║
╚════════════════════════════════════════════╝
Status: Filter: OFF | Recommendations: OFF

┌─────────────────────────────────────────────┐
│  1) View Internships                        │ ──┐
│  2) Configure Filters                       │   │
│  3) Configure Recommendations               │   │  Main Features
│  4) Toggle Filter Mode (Currently: OFF)     │   │
│  5) Toggle Recommendation Mode (OFF)        │   │
│  6) Clear All Filters & Settings            │ ──┘
│  7) Apply for Internship                    │
│  8) Show Application Status / Manage Offers │
│  9) Change Password                         │
│  0) Logout                                  │
└─────────────────────────────────────────────┘
```

## Feature Details

### Option 1: View Internships

```
┌──────────────────────────┐
│    View Internships      │
└──────────────────────────┘
           │
           ├─► Step 1: Get all eligible internships
           │           (open status + year-based level check)
           │
           ├─► Step 2: Apply filters? (if Filter Mode ON)
           │           │
           │           ├─► YES → Filter by criteria
           │           │         └─► No results? Offer to show all
           │           └─► NO  → Use all eligible
           │
           ├─► Step 3: Apply ranking? (if Recommendation Mode ON)
           │           │
           │           ├─► YES → Rank by preferences
           │           └─► NO  → Sort alphabetically
           │
           └─► Display results with status information
```

### Option 2: Configure Filters

```
┌──────────────────────────────┐
│    Configure Filters         │
└──────────────────────────────┘
           │
           ├─► Enter keywords (optional)
           │
           ├─► Select level preferences (Y3+ only)
           │   ├─ Y1-2: Auto BASIC only
           │   └─ Y3+:  Choose 1+ levels
           │
           ├─► Major matching? (y/n)
           │
           ├─► Enter company filter (optional)
           │
           └─► Settings saved (not yet active)
               Use Option 4 to toggle ON
```

### Option 3: Configure Recommendations

```
┌──────────────────────────────┐
│  Configure Recommendations   │
└──────────────────────────────┘
           │
           ├─► Enter ranking keywords (optional)
           │
           ├─► Select level preferences (Y3+ only)
           │
           ├─► Prioritize major match? (y/n)
           │
           ├─► Prioritize closing soon? (y/n)
           │
           ├─► Set priority rankings:
           │   ├─ closingSoon:     [1-4]
           │   ├─ levelFit:        [1-4]
           │   ├─ preferredMajor:  [1-4]
           │   └─ keywords:        [1-4]
           │
           └─► Priorities saved (not yet active)
               Use Option 5 to toggle ON
```

### Option 4 & 5: Toggle Switches

```
┌──────────────────┬──────────────────┐
│  Toggle Filter   │  Toggle Ranking  │
└──────────────────┴──────────────────┘
       │                    │
       ├─► OFF → ON         ├─► OFF → ON
       │   (activate)       │   (activate)
       │                    │
       └─► ON → OFF         └─► ON → OFF
           (deactivate)         (deactivate)

Independent switches - can use either, both, or neither
```

### Option 6: Clear All Settings

```
┌──────────────────────────────┐
│   Clear All Settings         │
└──────────────────────────────┘
           │
           ├─► Confirm action (y/n)
           │
           └─► Clear everything:
               ├─ All filter settings
               ├─ All recommendation settings
               ├─ Toggle states → OFF
               └─ Reset to defaults
```

## State Combinations & Behaviors

| Filter | Recommendation | Result                                    |
|--------|----------------|-------------------------------------------|
| OFF    | OFF            | All eligible, alphabetical order          |
| ON     | OFF            | Filtered only, alphabetical order         |
| OFF    | ON             | All eligible, ranked by preferences       |
| ON     | ON             | Filtered and ranked (most powerful mode)  |

## Year-Based Restrictions

```
Year 1-2 Students               Year 3+ Students
┌──────────────────┐           ┌──────────────────┐
│  BASIC only      │           │  All levels:     │
│  (auto-enforced) │           │  - BASIC         │
│                  │           │  - INTERMEDIATE  │
│  Cannot select   │           │  - ADVANCED      │
│  other levels    │           │                  │
│                  │           │  Can configure   │
│                  │           │  preferences     │
└──────────────────┘           └──────────────────┘
```

## Data Flow

```
┌─────────────┐
│  All        │
│  Internships│
└──────┬──────┘
       │
       ├─► Filter: Open Status
       │
       ├─► Filter: Year-based Level Eligibility
       │   (Y1-2 = BASIC only, Y3+ = all)
       │
       ├─► Optional: Apply User Filters (if Filter ON)
       │   └─► keywords, levels, major, company
       │
       ├─► Optional: Apply Ranking (if Recommendations ON)
       │   └─► score by priorities and preferences
       │
       └─► Display to User
           └─► Table view with pagination
```

## Configuration Persistence

```
┌──────────────────────────────────┐
│  Within Session (Login → Logout) │
└──────────────────────────────────┘
           │
           ├─► Filter settings persist
           ├─► Recommendation settings persist
           ├─► Toggle states persist
           │
           └─► Lost on logout/exit
               (intentional - fresh start per session)
```

## Best Practices Flow

```
Recommended Usage Flow:

1. Login
   │
2. View Internships (Option 1)
   └─► Browse all available (baseline)
   
3. Configure Filters (Option 2)
   └─► Set basic criteria
   
4. Toggle Filter ON (Option 4)
   │
5. View Internships (Option 1)
   └─► See filtered results
   
6. Configure Recommendations (Option 3)
   └─► Set ranking priorities
   
7. Toggle Recommendations ON (Option 5)
   │
8. View Internships (Option 1)
   └─► See best matches at top!
   
9. Apply for Internship (Option 7)
   └─► Apply to top matches
```

## Error Handling

```
┌────────────────────────────┐
│  Common Scenarios          │
└────────────────────────────┘

No internships found after filtering?
  └─► Prompt: "Show all instead?"
      ├─ Yes: Show all eligible
      └─ No:  Return to menu

No recommendation config but toggle ON?
  └─► Warning: "Configure recommendations first"
      └─ Return to menu

No filter config but toggle ON?
  └─► Warning: "Configure filters first"
      └─ Return to menu

Invalid internship ID for application?
  └─► Error: "Internship not found"
      └─ Return to menu
```

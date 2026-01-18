# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Vue 3 food delivery platform frontend (饿了么商家后台管理系统) with three distinct user roles: regular users, business owners, and administrators. The application is built with Vue 3 Composition API, Element Plus, and Vue Router 4.

## Development Commands

```bash
# Install dependencies
npm install

# Start dev server (runs on http://localhost:8081)
npm run serve

# Build for production
npm run build

# Lint code
npm run lint

# Auto-fix linting issues
npm run lint -- --fix
```

## Architecture

### Three-Tier Role System

The application implements a role-based access control system with three user types:

1. **Regular Users (`type: 'user'`)**: Browse businesses, place orders, manage delivery addresses
2. **Business Owners (`type: 'business'`)**: Manage their business info and food items
3. **Administrators (`type: 'admin'`)**: Manage all businesses in the system

### Authentication Flow

Authentication is handled via `src/utils/auth.js` which manages localStorage-based session:

```javascript
// User data stored in localStorage:
- username: string
- token: string
- userType: 'admin' | 'business' | 'user'
- userId: string (extracted from token)
```

**Critical**: The auth system uses simple token extraction patterns:
- Admin tokens: `admin_token_{userId}`
- Business tokens: `business_token_{userId}`

The `userId` is extracted by removing these prefixes.

### Router Guard Logic

`src/router/index.js` implements a three-level authorization check:

1. **requiresAuth**: User must be logged in
2. **requiresAdmin**: User must have admin role
3. **requiresBusiness**: User must have business role

When unauthorized:
- Not logged in → redirect to `/login` with `?redirect={originalPath}`
- Insufficient privileges → redirect to `/` (Index)

### Component Organization

Components are **not** organized into subdirectories. All Vue components live directly in `src/components/`:

- **Auth**: `Login.vue`, `Register.vue`
- **Dashboards**: `AdminDashboard.vue`, `BusinessDashboard.vue`
- **Business**: `BusinessList.vue`, `BusinessInfo.vue`
- **Orders**: `Order.vue`, `OrderList.vue`, `Payment.vue`
- **Addresses**: `UserAddress.vue`, `AddUserAddress.vue`, `EditUserAddress.vue`
- **Common**: `Index.vue`, `Footer.vue`

### API Proxy Configuration

The dev server (`vue.config.js`) proxies `/api/*` to `http://localhost:8080`:

```javascript
'/api' → 'http://localhost:8080'
// Example: axios.post('/api/user/login') → http://localhost:8080/user/login
```

**Important**: Backend must be running on port 8080 for API calls to work.

### State Management

**No Vuex/Pinia**: This project does not use a centralized state management library. User state is managed through:
- `Auth` utility for authentication
- Local component state (Vue Composition API `ref`/`reactive`)
- Route parameters for data passing

### Element Plus Integration

All Element Plus components and icons are globally registered in `main.js`:

```javascript
// All icons registered globally
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
```

This means icons can be used directly without imports: `<User />`, `<Lock />`, `<Shop />`, etc.

## Key Patterns

### Login Flow

1. User submits credentials to `POST /api/user/login`
2. Response contains: `{ success, token, type, message }`
3. `userId` extracted from token using pattern matching
4. Auth data saved to localStorage via `Auth.setUser()`
5. Redirect based on `type`:
   - `admin` → `/admin`
   - `business` → `/business`
   - Default → `/` or `?redirect` query param

### Component Route Protection

Dashboard components double-check permissions in `onMounted`:

```javascript
onMounted(() => {
  if (!Auth.isAdmin()) {  // or Auth.isBusiness()
    router.push('/')
  }
})
```

This prevents manual URL navigation bypassing route guards.

### Mobile-First Design

The app uses mobile-first responsive design:
- Global breakpoint: `@media (max-width: 768px)`
- Touch optimization: `-webkit-tap-highlight-color: transparent`
- Font smoothing for better rendering
- Card-based layouts with hover effects

## Common Development Tasks

### Adding a New Route

1. Create component in `src/components/`
2. Import component in `src/router/index.js`
3. Add route object with appropriate `meta` flags:
   ```javascript
   {
     path: '/my-route',
     name: 'MyRoute',
     component: MyComponent,
     meta: {
       requiresAuth: true,      // optional
       requiresAdmin: false,    // optional
       requiresBusiness: false  // optional
     }
   }
   ```

### Making API Calls

All API calls should use the `/api` prefix (auto-proxied in dev):

```javascript
import axios from 'axios'

// Correct
await axios.get('/api/business/list')
await axios.post('/api/user/login', loginForm.value)

// Wrong - will not proxy
await axios.get('http://localhost:8080/business/list')
```

### Testing Different User Roles

Test credentials are hardcoded in `Login.vue`:
- Admin: `username: 'admin'`, `password: '123456'`
- Business: `username: '肯德基'`, `password: '123456'`

## Known Limitations

- **No automated tests**: The project has no test setup
- **localStorage auth**: Tokens in localStorage are vulnerable to XSS; consider httpOnly cookies for production
- **No loading states**: Some components lack proper loading/error handling
- **Hardcoded backend URL**: Backend address is fixed to `localhost:8080`
- **Missing features**: Some dashboard buttons show "use console" messages (placeholder features)

## Troubleshooting

**"Network Error" on login**: Backend is not running on port 8080

**Route redirects to `/` after login**: Check that the token format matches expected patterns and `userType` is set correctly

**Icons not showing**: Ensure `@element-plus/icons-vue` is installed and registered in `main.js`

**CORS errors**: Use the `/api` prefix for all backend calls to leverage the dev proxy

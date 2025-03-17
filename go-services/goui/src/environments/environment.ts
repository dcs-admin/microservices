export const environment = {
    production: false,
    //apiBaseUrl: 'http://localhost:2000/api', 
    authEndpoints: {
      baseUrl: 'http://localhost:8888/api',
      register: '/auth/register',
      login: '/auth/login',
    },
    productEndpoints: {
      baseUrl: 'http://localhost:2000/api',
      getAll: '/products',
      getById: '/products/', // Append ID when calling
    },
    customerEndpoints: {
      baseUrl: 'http://localhost:1000/api',
      getAll: '/customers',
      getById: '/customers/', // Append ID when calling
    },
    orderEndpoints: {
      baseUrl: 'http://localhost:3000/api',
      getAll: '/orders',
      getById: '/orders/', // Append ID when calling
    },
    defaultHeaders: {
      Authorization: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InRlc3R1c2VyIiwiZXhwIjoxNzQyMDQzNTc1fQ.0qa9Jh0c2bQHTeh2tJtRUPFaR80H5ZV2K3xKq-CFw10',
      'Content-Type': 'application/json',
    },
  };
  
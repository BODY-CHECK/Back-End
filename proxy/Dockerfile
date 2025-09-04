FROM nginx:latest

WORKDIR /etc/nginx

COPY proxy/nginx.conf /etc/nginx/nginx.conf

COPY proxy/conf.d/ /etc/nginx/conf.d/

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]